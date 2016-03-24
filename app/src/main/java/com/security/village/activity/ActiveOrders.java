package com.security.village.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.security.village.HttpErrorHandler;
import com.security.village.ObjectMap;
import com.security.village.R;
import com.security.village.adapter.OrdersAdapter;
import com.security.village.settingsholder.AppSettingsProvider;
import com.security.village.settingsholder.Keys;
import com.security.village.webservice.retrofit.RestModuleNew;
import com.security.village.webservice.retrofit.response.Orders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by fruitware on 12/23/15.
 */
public class ActiveOrders extends Activity implements OrdersAdapter.OnOrderClickListener, SwipeRefreshLayout.OnRefreshListener{

    private static final String ACTIVE_ORDERS = "api/v1/security/services/orders";

    private OrdersAdapter adapter;
    private ListView ordersList;
    private TextView hint;
    private EditText searchBar;
    private ImageView rightButton;
    private ImageView addOrderButton;
    private SwipeRefreshLayout swipeLayout;
    private FrameLayout my_profile_button;

    private HashMap<String, String> map;
    private List<Orders.Data> list;
    private int PAGE = 1;
    private boolean isLastPageEmpty = false;
    private int recentDay;
    private int recentMonth;
    private int recentYear;

    private FrameLayout myProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_orders);
        map = new HashMap<>();
        my_profile_button = (FrameLayout) findViewById(R.id.my_profile_button);
        map.put("status", "processing");
        map.put("page", Integer.toString(PAGE));
        list = new ArrayList<>();
        adapter = new OrdersAdapter(this, 0, false, OrdersAdapter.CUT);
        adapter.setActivityClass(ActiveOrders.class);
        adapter.setListener(this);

        recentDay += Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        recentMonth += Calendar.getInstance().get(Calendar.MONTH)+1;
        recentYear += Calendar.getInstance().get(Calendar.YEAR);

        map.put("from_perform_date", Integer.toString(recentYear) + "-" + Integer.toString(recentMonth) + "-" + Integer.toString(recentDay));

        my_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                startActivity(intent);
            }
        });

        initializeViews();
        getOrders();
    }

    @Override
    public void onRefresh() {
            refreshList();
    }

    private void initializeViews(){
        myProfileButton = (FrameLayout) findViewById(R.id.my_profile_button);
        ordersList = (ListView) findViewById(R.id.active_orders_list);
        rightButton = (ImageView) findViewById(R.id.right_button);
        hint = (TextView) findViewById(R.id.hint);
        addOrderButton = (ImageView) findViewById(R.id.add_order);
        searchBar = (EditText) findViewById(R.id.search_bar_active);

        ordersList.setAdapter(adapter);
        ordersList.setOnScrollListener(onScroll);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.setRefreshing(true);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActiveOrders.this, AllOrders.class);
                startActivity(intent);
            }
        });

        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActiveOrders.this, AddOrder.class);
                intent.putExtra(Keys.REFRESH_CLASS, ActiveOrders.class.getName());
                ActiveOrders.this.startActivityForResult(intent, Keys.REFRESH);
            }
        });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    hideKeyBoard();

                    map.put("search", searchBar.getText().toString());
                    refreshList();
                    getOrders();
                    return true;
                }
                return false;
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 2) {
                    refreshList();
                    map.put("search", searchBar.getText().toString());
                    getOrders();
                } else if (editable.length() == 0) {
                    refreshList();
                    map.remove("search");
                    getOrders();
                }
            }
        });
    }

    private void hideKeyBoard(){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager)  ActiveOrders.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(ActiveOrders.this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshList(){
        swipeLayout.setRefreshing(true);
        list.clear();
        adapter.notifyDataSetChanged();
        PAGE = 1;
        isLastPageEmpty = false;
        map.put("page", Integer.toString(PAGE));
        getOrders();
    }

    private AbsListView.OnScrollListener onScroll = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if (scrollState == SCROLL_STATE_IDLE) {
                if (isLastPageEmpty) {
                    return;
                }
                PAGE++;
                map.put("page", Integer.toString(PAGE));
                getOrders();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };

    private void getOrders(){
        RestModuleNew.provideRestService().getAuth(ACTIVE_ORDERS, AppSettingsProvider.getInstance().getToken(ActiveOrders.this), map, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                try {
                    Orders orders = ObjectMap.getInstance().readValue(s, Orders.class);
                    visualizeOrders(orders);
                } catch (IOException e) {
                    swipeLayout.setRefreshing(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                swipeLayout.setRefreshing(false);
                toast(HttpErrorHandler.handleError(error), AppSettingsProvider.getInstance().getToken(ActiveOrders.this));
            }
        });
    }

    public void visualizeOrders(Orders orders){
        List<Orders.Data> listTmp = new ArrayList<>();
        for(Orders.Data x : orders.getData()){
            list.add(x);
            listTmp.add(x);
        }

        if(listTmp.size() < 10){
            isLastPageEmpty = true;
        }


        if(list.size() != 0){
            hint.setVisibility(View.GONE);
        } else {
            hint.setVisibility(View.VISIBLE);
        }

        adapter.setListOfData(list);
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    public void toast(String str, String token){
        if (str != null) {
            if(str.equalsIgnoreCase("token_invalid")){
                RestModuleNew.refreshToken(ActiveOrders.this, token);
                return;
            }else
                Toast.makeText(ActiveOrders.this, str, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOrderClickListener(int id, Class clasS) {
        Intent intent = new Intent(this, OneOrder.class);
        intent.putExtra(Keys.ORDER, list.get(id));
        intent.putExtra(Keys.ORDER_ID, list.get(id).getId());
        intent.putExtra(Keys.REFRESH_CLASS, clasS.getName());
        this.startActivityForResult(intent, Keys.REFRESH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Keys.REFRESH || requestCode == Keys.REFRESH){
            refreshList();
        }
    }
}
