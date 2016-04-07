package com.security.village.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
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
import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by fruitware on 12/23/15.
 */
public class AllOrders extends Activity implements OrdersAdapter.OnOrderClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String ALL_ORDERS = "api/v1/security/services/orders";

    private OrdersAdapter adapter;
    private ListView ordersList;
    private ImageView rightButton;
    private TextView title;
    private SwipeRefreshLayout swipeLayout;
    private TextView hint;
    private ImageView leftButton;
    private TextView activeOrders;
    private EditText searchBar;

    private Calendar calendar;

    private HashMap<String, String> map;
    private List<Orders.Data> list;
    private int PAGE = 1;
    private int LAST_PAGE = 0;
    private boolean isLastPageEmpty = false;
    private float initialY;
    private int recentDay;
    private int recentMonth;
    private int recentYear;
    private String lastDay;
    private String lastMonth;
    private String lastYear;
    private String lastString = "";
    private long milisec, lastSec, lastAutoRefresh;
    private long MIN_DT = 500;

    private boolean isScrolling, isRefreshing, isSearching, isChangingDate;

    private Handler handler;
    private Runnable refreshList, setRefreshOnFalse, isChangingDateRun, isSearchingRunFalse, setSearchOnFalse, manualRefresh, fullRefresh, searchCompare, switchDate;

    private void disableAllRunnables(){
        if(setRefreshOnFalse != null)
            handler.removeCallbacks(setRefreshOnFalse);
        if(isChangingDateRun != null)
            handler.removeCallbacks(isChangingDateRun);
        if(isSearchingRunFalse != null)
            handler.removeCallbacks(isSearchingRunFalse);
        if(setSearchOnFalse != null)
            handler.removeCallbacks(setSearchOnFalse);
        if(searchCompare != null)
            handler.removeCallbacks(searchCompare);
        if(switchDate != null)
            handler.removeCallbacks(switchDate);
        if(fullRefresh != null)
            handler.removeCallbacks(fullRefresh);

        isScrolling = isRefreshing = isSearching = isChangingDate = false;

    }
    private AbsListView.OnScrollListener onScroll = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if (scrollState == SCROLL_STATE_IDLE) {
                isScrolling = true;
                if (isLastPageEmpty) {
                    isScrolling = false;
                    return;
                }
                PAGE++;
                getOrders(PAGE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isScrolling = false;
                    }
                }, 750);
            } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_orders);
        handler = new Handler();
        map = new HashMap<>();
        map.put("only_opened", "0");
        map.put("page", Integer.toString(PAGE));
        list = new ArrayList<>();
        calendar = Calendar.getInstance();
        adapter = new OrdersAdapter(this, 0, true, OrdersAdapter.ALL);
        adapter.setActivityClass(AllOrders.class);
        adapter.setListener(this);

        setRefreshOnFalse = new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                isRefreshing = false;
            }
        };

        recentDay += Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        recentMonth += Calendar.getInstance().get(Calendar.MONTH) + 1;
        recentYear += Calendar.getInstance().get(Calendar.YEAR);

        initializeViews();
        changeDate(Keys.DATE_THAT_DOESNT_EXIST);
        getOrders(PAGE);

    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        refreshList();
    }

    private void initializeViews() {
        title = (TextView) findViewById(R.id.title);
        ordersList = (ListView) findViewById(R.id.all_orders_list);
        rightButton = (ImageView) findViewById(R.id.right_button);
        hint = (TextView) findViewById(R.id.hint);
        leftButton = (ImageView) findViewById(R.id.left_button);
        activeOrders = (TextView) findViewById(R.id.active_orders_button);
        searchBar = (EditText) findViewById(R.id.search_bar);
        title.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + "." + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendar.get(Calendar.YEAR)).substring(2));

        ordersList.setAdapter(adapter);
        ordersList.setOnScrollListener(onScroll);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.setRefreshing(true);

        activeOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PAGE = 1;
                swipeLayout.setRefreshing(true);

                disableAllRunnables();
                isChangingDate = true;
                if(changeDate(calendar.get(Calendar.DAY_OF_MONTH) + 1)) {

                    final long curTime = System.currentTimeMillis();
                    if (curTime - lastSec > 500) {
                        isChangingDateRun = new Runnable() {
                            @Override
                            public void run() {
                                isChangingDate = false;
                            }
                        };
                        handler.postDelayed(isChangingDateRun, 800);
                        lastSec = curTime;
                        Log.w("TRACE_ALL", "plain search curTime - lastSec > 500");
                        list.clear();
                        adapter.notifyDataSetChanged();
                        getOrders(PAGE);
                    } else {
                        if (curTime - lastSec < 200) {

                            switchDate = new Runnable() {
                                @Override
                                public void run() {
                                    isChangingDate = false;
                                    lastSec = curTime;
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    Log.w("TRACE_ALL", "compare search handler");
                                    getOrders(PAGE);
                                }
                            };

                            handler.postDelayed(switchDate, 1100);

                        } else {
                            switchDate = new Runnable() {
                                @Override
                                public void run() {
                                    isChangingDate = false;
                                    lastSec = curTime;
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    Log.w("TRACE_ALL", "plain search handler");
                                    getOrders(PAGE);
                                }
                            };
                            handler.postDelayed(switchDate, 1000);
                        }
                    }
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PAGE = 1;
                swipeLayout.setRefreshing(true);

                disableAllRunnables();
                isChangingDate = true;
                if(changeDate(calendar.get(Calendar.DAY_OF_MONTH) - 1)){
                    final long curTime = System.currentTimeMillis();
                    if (curTime - lastSec > 500) {
                        isChangingDateRun = new Runnable() {
                            @Override
                            public void run() {
                                isChangingDate = false;
                            }
                        };
                        handler.postDelayed(isChangingDateRun, 800);
                        lastSec = curTime;
                        Log.w("TRACE_ALL", "plain search curTime - lastSec > 500");
                        list.clear();
                        adapter.notifyDataSetChanged();
                        getOrders(PAGE);
                    } else {
                        if(curTime - lastSec < 200){
                            switchDate = new Runnable() {
                                @Override
                                public void run() {
                                    isChangingDate = false;
                                    lastSec = curTime;
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    Log.w("TRACE_ALL", "compare search handler");
                                    getOrders(PAGE);
                                }
                            };

                            handler.postDelayed(switchDate, 1100);

                        } else {

                            switchDate = new Runnable() {
                                @Override
                                public void run() {
                                    isChangingDate = false;
                                    lastSec = curTime;
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    Log.w("TRACE_ALL", "plain search handler");
                                    getOrders(PAGE);
                                }
                            };
                            handler.postDelayed(switchDate, 1000);
                        }
                    }
                }


            }
        });

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {

                    hideKeyBoard();

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
            public void afterTextChanged(final Editable editable) {
                disableAllRunnables();
                PAGE = 1;
                swipeLayout.setRefreshing(true);
                isSearching = true;
                if (editable.length() > 2) {
                    map.put("search", searchBar.getText().toString());
                    final long curTime = System.currentTimeMillis();
                    if (curTime - lastSec > 500) {
                        isSearchingRunFalse = new Runnable() {
                            @Override
                            public void run() {
                                if(isSearching){
                                    lastSec = curTime;
                                    Log.w("TRACE_ACTIVE", "plain search curTime - lastSec > 500");
                                    list.clear();
                                    adapter.notifyDataSetChanged();
                                    getOrders(PAGE);
                                }

                                isSearching = false;
                            }
                        };
                        handler.postDelayed(isSearchingRunFalse, 800);
                    } else {
                        if(curTime - lastSec < 100){

                            searchCompare = new Runnable() {
                                @Override
                                public void run() {
                                    if (lastString.equalsIgnoreCase(editable.toString()) && editable.length() > 2) {
                                        if(isSearching) {
                                            map.put("search", editable.toString());
                                            lastSec = curTime;
                                            list.clear();
                                            adapter.notifyDataSetChanged();
                                            Log.w("TRACE_ACTIVE", "compare search handler");
                                            getOrders(PAGE);
                                        }
                                        isSearching = false;
                                    } else if (lastString.length() < 2) {
                                        if (setSearchOnFalse != null)
                                            handler.removeCallbacks(setSearchOnFalse);
                                        map.remove("search");

                                        if(isSearching)
                                            refreshList();

                                        isSearching = false;
                                    }

                                    lastString = editable.toString();
                                }
                            };
                            if (lastString.equalsIgnoreCase("")) {
                                lastString = editable.toString();
                            }

                            if(editable.length() == 0)
                                lastString = "";

                            if (setSearchOnFalse != null) {
                                handler.removeCallbacks(setSearchOnFalse);
                            }
                            if (searchCompare != null) {
                                handler.removeCallbacks(searchCompare);
                            }

                            handler.postDelayed(searchCompare, 900);

                        } else {
                            if (setSearchOnFalse != null) {
                                handler.removeCallbacks(setSearchOnFalse);
                            }
                            if (searchCompare != null) {
                                handler.removeCallbacks(searchCompare);
                            }

                            lastString = "";

                            setSearchOnFalse = new Runnable() {
                                @Override
                                public void run() {
                                    if(isSearching){
                                        lastSec = curTime;
                                        list.clear();
                                        adapter.notifyDataSetChanged();
                                        Log.w("TRACE_ACTIVE", "plain search handler");
                                        getOrders(PAGE);
                                    }
                                    isSearching = false;
                                }
                            };
                            handler.postDelayed(setSearchOnFalse, 1000);
                        }
                    }
                } else {
                    if (setSearchOnFalse != null) {
                        handler.removeCallbacks(setSearchOnFalse);
                    }
                    if (searchCompare != null) {
                        handler.removeCallbacks(searchCompare);
                    }

                    if (fullRefresh != null) {
                        handler.removeCallbacks(fullRefresh);
                    }

                    lastString = "";

                    map.remove("search");

                    fullRefresh = new Runnable() {
                        @Override
                        public void run() {
                            if(isSearching)
                                refreshList();
                            isSearching = false;
                        }
                    };

                    handler.postDelayed(fullRefresh, 1800);
                }
            }
        });
    }

    private void hideKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) AllOrders.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(AllOrders.this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean changeDate(int day) {
        Log.d("lastDate", "called changeDate();" + " with 'int day = " + Integer.toString(day));
        if (day != Keys.DATE_THAT_DOESNT_EXIST) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        String resultDay;
        String resultMonth;
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            resultDay = "0" + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            resultDay = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        }
        if (calendar.get(Calendar.MONTH) + 1 < 10) {
            resultMonth = "0" + Integer.toString(calendar.get(Calendar.MONTH) + 1);
        } else {
            resultMonth = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        }
        title.setText(resultDay + "-" + resultMonth + "-" + Integer.toString(calendar.get(Calendar.YEAR)));
        map.put("from_perform_date", Integer.toString(calendar.get(Calendar.YEAR)) + "-" + resultMonth + "-" + resultDay);
        map.put("to_perform_date", Integer.toString(calendar.get(Calendar.YEAR)) + "-" + resultMonth + "-" + resultDay);
        return true;
    }

    private void refreshList() {
        isRefreshing = true;
        swipeLayout.setRefreshing(true);
        PAGE = 1;
        isLastPageEmpty = false;
        list.clear();
        adapter.notifyDataSetChanged();
        getOrders(PAGE);
        handler.removeCallbacks(setRefreshOnFalse);
        handler.postDelayed(setRefreshOnFalse, 1500);
    }

    private void getOrders(final int PAGE) {
        if (PAGE == 0) {
            map.put("page", "1");
            if(list.size() != 0){
                map.put("limit", Integer.toString(list.size()));
            }
        } else {
            map.put("page", Integer.toString(PAGE));
            map.remove("limit");
        }

        RestModuleNew.provideRestService().getAuth(ALL_ORDERS, AppSettingsProvider.getInstance().getToken(AllOrders.this), map, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                try {
                    Orders orders = ObjectMap.getInstance().readValue(s, Orders.class);
                    if (PAGE == 0) {
                        list.clear();
                        for (Orders.Data x : orders.getData()) {
                            list.add(x);
                        }

                        if (list.size() != 0 || orders.getData().length > 0) {
                            hint.setVisibility(View.GONE);
                        } else {
                            hint.setVisibility(View.VISIBLE);
                        }

                        if (list.size() > 10) {
                            isLastPageEmpty = false;
                        }

                        adapter.notifyDataSetChanged();
                        swipeLayout.setRefreshing(false);
                    } else {
                        visualizeOrders(orders);
                    }
                } catch (IOException e) {
                    swipeLayout.setRefreshing(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                swipeLayout.setRefreshing(false);
                toast(HttpErrorHandler.handleError(error));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList = new Runnable() {
            @Override
            public void run() {
                if(!isRefreshing && !isScrolling && !isSearching && !isChangingDate) {
                    Log.w("TRACE_ALL", "auto refresh = ON");
                    getOrders(0);
                    lastAutoRefresh = System.currentTimeMillis();
                }

                if(System.currentTimeMillis() - lastAutoRefresh > AppSettingsProvider.getInstance().getRefreshListTime(getApplicationContext()) * 1000 && lastAutoRefresh != 0){
                    disableAllRunnables();
                }
                Log.w("REFRESH_ALL", Integer.toString(AppSettingsProvider.getInstance().getRefreshListTime(getApplicationContext())));
                handler.postDelayed(this, AppSettingsProvider.getInstance().getRefreshListTime(getApplicationContext()) * 1000);

            }
        };

        handler.postDelayed(refreshList, AppSettingsProvider.getInstance().getRefreshListTime(getApplicationContext()) * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public void visualizeOrders(Orders orders) {
        List<Orders.Data> listTmp = new ArrayList<>();
        for (Orders.Data x : orders.getData()) {
            hint.setVisibility(View.GONE);
            if (list.size() == 0) {
                list.add(x);
            } else {
                for (Iterator<Orders.Data> dd = list.iterator(); list.iterator().hasNext(); ) {
                    if (list.size() != 0) {
                        try {
                            if (!dd.next().getId().equalsIgnoreCase(x.getId())) {
                                list.add(x);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            listTmp.add(x);
        }

        if (list.size() != 0) {
            hint.setVisibility(View.GONE);
        } else {
            hint.setVisibility(View.VISIBLE);
        }

        adapter.setListOfData(list);
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);

//        if(calendar.get(Calendar.DAY_OF_MONTH) == recentDay && calendar.get(Calendar.MONTH)+1 == recentMonth && calendar.get(Calendar.YEAR) == recentYear)
//            rightButton.setBackgroundColor(Color.parseColor("#d1d1d1"));
//        else
//            rightButton.setBackgroundColor(Color.parseColor("#F96332"));
    }

    public void toast(String str) {
        if (str != null) {
            if (str.equalsIgnoreCase("token_invalid")) {
                RestModuleNew.refreshToken(AllOrders.this, AppSettingsProvider.getInstance().getToken(AllOrders.this));
                return;
            } else
                Toast.makeText(AllOrders.this, str, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOrderClickListener(int id, Class clasS) {
        Intent intent = new Intent(this, OneOrder.class);
        intent.putExtra(Keys.ORDER, list.get(id));
        intent.putExtra(Keys.ORDER_ID, list.get(id).getId());
        intent.putExtra(Keys.REFRESH_CLASS, clasS.getName());
        intent.putExtra(Keys.REFRESH_DATE, map.get("from_perform_date"));
        this.startActivityForResult(intent, Keys.REFRESH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Keys.REFRESH || requestCode == Keys.REFRESH) {
            try {
                int id = Integer.parseInt(data.getStringExtra(Keys.ORDER_ID));
                String payment = data.getStringExtra(Keys.PAYMENT_STATUS);
                String status = data.getStringExtra(Keys.STATUS);
                for (Orders.Data x : list) {
                    if (Integer.parseInt(x.getId()) == id) {
                        if (payment == null)
                            payment = x.getPayment_status();
                        if (status == null)
                            payment = x.getStatus();

                        x.setPayment_status(payment);
                        x.setStatus(status);
                    }
                }
                adapter.notifyDataSetChanged();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                refreshList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
