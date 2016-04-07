package com.security.village.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.security.village.DateUtils;
import com.security.village.HttpErrorHandler;
import com.security.village.ObjectMap;
import com.security.village.R;
import com.security.village.settingsholder.AppSettingsProvider;
import com.security.village.settingsholder.Keys;
import com.security.village.webservice.retrofit.RestModuleNew;
import com.security.village.webservice.retrofit.response.Orders;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by fruitware on 12/24/15.
 */
public class OneOrder extends Activity {
    private static final String ONE_ORDER = "api/v1/security/services/orders/";
    private static final String UPDATE_ORDER = "api/v1/security/services/orders/";
    private String orderId;
    private String classParent;
    private String performDate;
    private Orders.Data order;

    private HashMap<String, String> map;

    private ImageView arrowLeft;
    private TextView service;
    private TextView dateTime;
    private TextView dateTimeLabel;
    private TextView declarer;
    private TextView object;
    private TextView info;
    private TextView paymentTxt;
    private TextView phone;
    private TextView phoneLabel;
    private RelativeLayout arrivedLayout;
    private RelativeLayout paymentLayout;
    private CheckBox payment;
    private CheckBox arrived;
    private Button complete;
    private LinearLayout mainLayout;
    private SwipeRefreshLayout swipeLayout;

    private EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_order_activity);
        orderId = getIntent().getStringExtra(Keys.ORDER_ID);
        classParent = getIntent().getStringExtra(Keys.REFRESH_CLASS);
        performDate = getIntent().getStringExtra(Keys.REFRESH_DATE);
        map = new HashMap<>();
        initializeViews();
        getOrderInfo();
    }

    private void initializeViews() {
        mainLayout = (LinearLayout) findViewById(R.id.layout_holder);
        mainLayout.setVisibility(View.INVISIBLE);
        arrowLeft = (ImageView) findViewById(R.id.left_button);
        declarer = (TextView) findViewById(R.id.declarer);
        service = (TextView) findViewById(R.id.service);
        dateTime = (TextView) findViewById(R.id.date_time);
        dateTimeLabel = (TextView) findViewById(R.id.date_time_label);
        object = (TextView) findViewById(R.id.object);
        info = (TextView) findViewById(R.id.info);
        phone = (TextView) findViewById(R.id.phone);
        phoneLabel = (TextView) findViewById(R.id.textViews);
        payment = (CheckBox) findViewById(R.id.payment);
        paymentTxt = (TextView) findViewById(R.id.payment_txt);
        arrived = (CheckBox) findViewById(R.id.arrived);
        arrivedLayout = (RelativeLayout) findViewById(R.id.arrived_layout);
        paymentLayout = (RelativeLayout) findViewById(R.id.payment_layout);
        complete = (Button) findViewById(R.id.complete);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        comment = (EditText) findViewById(R.id.comment);

        swipeLayout.setEnabled(false);

//        if(Integer.parseInt(AppSettingsProvider.getInstance().getSdkVersion(OneOrder.this)) <= 10 ){
//            payment.setPadding(payment.getPaddingLeft() + 26,
//                    payment.getPaddingTop(),
//                    payment.getPaddingRight(),
//                    payment.getPaddingBottom());
//
//            arrived.setPadding(arrived.getPaddingLeft() + 26,
//                    arrived.getPaddingTop(),
//                    arrived.getPaddingRight(),
//                    arrived.getPaddingBottom());
//        }

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeLayout.setRefreshing(true);
                if (map.get("payment_status").equalsIgnoreCase("paid")) {
                    complete.setEnabled(false);
                    sendRequest();
                } else {
                    toast("Заявка должна быть оплачена");
                    swipeLayout.setRefreshing(false);
                }
            }
        });

        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payment.isChecked()) {
                    map.put("payment_status", "paid");
                } else {
                    map.put("payment_status", "not_paid");
                }
                complete.setEnabled(true);
            }
        });

        paymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!payment.isChecked()) {
                    payment.setChecked(true);
                    map.put("payment_status", "paid");
                } else {
                    payment.setChecked(false);
                    map.put("payment_status", "not_paid");
                }
                complete.setEnabled(true);
            }
        });

        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrived.isChecked()) {
                    map.put("status", "done");
                } else {
                    map.put("status", "processing");
                }
                complete.setEnabled(true);
            }
        });

        arrivedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!arrived.isChecked()) {
                    arrived.setChecked(true);
                    map.put("status", "done");
                } else {
                    arrived.setChecked(false);
                    map.put("status", "processing");
                }
                complete.setEnabled(true);
            }
        });
    }

    public void sendRequest() {
        if (comment.getText().length() > 0) {
            map.put("admin_comment", comment.getText().toString());
        }
        RestModuleNew.provideRestService().patch(UPDATE_ORDER + orderId, AppSettingsProvider.getInstance().getToken(OneOrder.this), map, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                swipeLayout.setRefreshing(false);
                try {
                    Intent intent = new Intent(OneOrder.this, Class.forName(classParent));
                    intent.putExtra(Keys.REFRESH_DATE, performDate + "");
                    intent.putExtra(Keys.ORDER_ID, orderId + "");
                    intent.putExtra(Keys.PAYMENT_STATUS, map.get("payment_status"));
                    intent.putExtra(Keys.STATUS, map.get("status"));
                    setResult(Keys.REFRESH, intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                onBackPressed();
            }

            @Override
            public void failure(RetrofitError error) {
                swipeLayout.setRefreshing(false);
                toast(HttpErrorHandler.handleError(error));
                complete.setEnabled(true);
            }
        });
    }

    private void getOrderInfo() {
        RestModuleNew.provideRestService().getAuth(ONE_ORDER + orderId, AppSettingsProvider.getInstance().getToken(this), null, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                swipeLayout.setRefreshing(false);
                try {
                    Orders.Order order = ObjectMap.getInstance().readValue(s, Orders.Order.class);
                    viewOrder(order);
                } catch (IOException e) {
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

    private void viewOrder(Orders.Order data) {
        Orders.Data order = data.getData();
        map.put("status", order.getStatus());
        map.put("payment_status", order.getPayment_status());
        if (order.getPayment_status().equalsIgnoreCase(Keys.PAID)) {
            payment.setChecked(true);
            payment.setEnabled(false);
            paymentLayout.setEnabled(false);
        }
        if (Float.parseFloat(order.getPrice()) == 0) {
            info.setText("Бесплатно");
            paymentLayout.setVisibility(View.GONE);
        } else {
            info.setText(order.getPrice().substring(0, order.getPrice().indexOf(".")) + " рублей");
            if (order.getPayment_type().equalsIgnoreCase("card")) {
                if (order.getPayment_status().equalsIgnoreCase(Keys.PAID)) {
                    paymentTxt.setText("Оплата on-line");
                    payment.setEnabled(false);
                    payment.setChecked(true);
                } else {
                    paymentTxt.setText("Оплатить на месте");
                }
            } else {
                paymentTxt.setText("Заказ оплачен");
            }
        }

        if (order.getStatus().equalsIgnoreCase(Keys.DONE)) {
            arrived.setChecked(true);
            complete.setVisibility(View.GONE);
            payment.setEnabled(false);
            arrived.setEnabled(false);
            arrivedLayout.setEnabled(false);
            paymentLayout.setEnabled(false);
            comment.setEnabled(false);
        }

        String dateTime = "";

        if (order.getPerform_date() != null) {
            dateTime += DateUtils.formatDate(order.getPerform_date(), DateUtils.DATE_FORMAT_7, DateUtils.DATE_FORMAT_22);
            if (order.getPerform_time() != null)
                dateTime += " " + order.getPerform_time();
        }

        try {
            if (dateTime.length() != 0 && !dateTime.equalsIgnoreCase(""))
                this.dateTime.setText(dateTime);
            else {
                this.dateTime.setVisibility(View.GONE);
                this.dateTimeLabel.setVisibility(View.GONE);
            }


            if (order.getAdded_from() != null && !order.getAdded_from().equals("")) {
                declarer.setText(order.getAdded_from());
            } else {
                String d = order.getUser().getData().getFirst_name().trim() + " " + order.getUser().getData().getLast_name().trim()
                        + ((order.getUser().getData().getPhone() != null) ? ", " + order.getUser().getData().getPhone() : "")
                        + ((order.getUser().getData().getBuilding().getData().getAddress() != null) ? ", " + order.getUser().getData().getBuilding().getData().getAddress() : "");
//                        + ((order.getUser().getData().getBuilding().getData().getVillage().getData().getName() != null) ? ", " + order.getUser().getData().getBuilding().getData().getVillage().getData().getName() : "")
//                        + ((order.getUser().getData().getBuilding().getData().getVillage().getData().getShop_name() != null) ? ", " + order.getUser().getData().getBuilding().getData().getVillage().getData().getShop_name() : "")
//                        + ((order.getUser().getData().getBuilding().getData().getVillage().getData().getShop_address() != null) ? ", " + order.getUser().getData().getBuilding().getData().getVillage().getData().getShop_address() : "");
                declarer.setText(d);
            }
        } catch (Exception e) {
            e.printStackTrace();


        }
        service.setText(order.getService().getData().getTitle());
        object.setText(order.getComment());

        if (order.getAdmin_comment() != null) {
            comment.setText(order.getAdmin_comment());
        }

        if (order.getPhone() != null) {
            phone.setText(order.getPhone());
        } else {
            phone.setVisibility(View.GONE);
            phoneLabel.setVisibility(View.GONE);
        }

        mainLayout.setVisibility(View.VISIBLE);
        swipeLayout.setRefreshing(false);
    }

    public void toast(String str) {
        if (str != null) {
            if (str.equalsIgnoreCase("token_invalid")) {
                RestModuleNew.refreshToken(OneOrder.this, AppSettingsProvider.getInstance().getToken(OneOrder.this));
                return;
            } else
                Toast.makeText(OneOrder.this, str, Toast.LENGTH_SHORT).show();
        }
    }
}
