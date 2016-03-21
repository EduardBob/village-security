package com.security.village.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.security.village.DateTimePicker;
import com.security.village.DateUtils;
import com.security.village.HttpErrorHandler;
import com.security.village.ObjectMap;
import com.security.village.R;
import com.security.village.settingsholder.AppSettingsProvider;
import com.security.village.settingsholder.Keys;
import com.security.village.webservice.retrofit.RestModuleNew;
import com.security.village.webservice.retrofit.response.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by fruitware on 12/24/15.
 */
public class AddOrder extends Activity implements View.OnTouchListener{
    private static final String REG_DATA_EXPRESSION_1 = "^\\d\\d\\d\\d-\\d\\d-\\d\\d$";
    private static final String REG_DATA_EXPRESSION_2 = "^\\d\\d:\\d\\d$";

    private static final String SERVICES = "api/v1/security/services";
    private static final String CREATE_ORDER = "api/v1/security/services/orders";
    private Spinner serviceSpinner;
    private List<String> serviceIds;
    private Button dateTimePicker;
    private EditText pickedDate;
    private EditText pickedTime;
    private EditText from;
    private EditText to;
    private CheckBox rights;
    private TextView rightsTxt;
    private Button complete;
    private ImageView arrowLeft;
    private RelativeLayout mainLayout;
    private Calendar calendar;
    private SwipeRefreshLayout swipeLayout;

    private HashMap<String, String> map;
    private String classParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        classParent = getIntent().getStringExtra(Keys.REFRESH_CLASS);
        setContentView(R.layout.add_order);
        serviceIds = new ArrayList<>();
        calendar = Calendar.getInstance();
        map = new HashMap<>();
        initializeViews();
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(AddOrder.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void initializeViews(){
        serviceSpinner = (Spinner) findViewById(R.id.service);
        dateTimePicker = (Button) findViewById(R.id.date_time_picker);
        pickedDate = (EditText) findViewById(R.id.date_string);
        pickedTime = (EditText) findViewById(R.id.time_string);
        from = (EditText) findViewById(R.id.from);
        to = (EditText) findViewById(R.id.to);
        rights = (CheckBox) findViewById(R.id.rights);
        rightsTxt = (TextView) findViewById(R.id.rights_txt);
        complete = (Button) findViewById(R.id.complete);
        arrowLeft = (ImageView) findViewById(R.id.left_button);
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeLayout.setRefreshing(true);
        swipeLayout.setEnabled(false);

        setupUI(mainLayout);

        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        complete.setEnabled(false);

//        if(Integer.parseInt(AppSettingsProvider.getInstance().getSdkVersion(AddOrder.this)) <= 10 ){
//            rights.setPadding(rights.getPaddingLeft() + 26,
//                    rights.getPaddingTop(),
//                    rights.getPaddingRight(),
//                    rights.getPaddingBottom());
//        }



        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddOrder.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                map.put("service_id", serviceIds.get((int) id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                AddOrder.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        });

        dateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(pickedDate);
            }
        });

        pickedDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(pickedDate.getText()) && pickedDate.getText().toString().length() == 10)
                    pickedTime.requestFocus();
                if (!TextUtils.isEmpty(pickedDate.getText()) && !TextUtils.isEmpty(to.getText()) && !TextUtils.isEmpty(from.getText()) && rights.isChecked()) {
                    map.put("perform_date",
                            DateUtils.formatDate(pickedDate.getText().toString(), DateUtils.DATE_FORMAT_22, DateUtils.DATE_FORMAT_7));
                    complete.setEnabled(true);
                } else {
                    map.put("perform_date", DateUtils.formatDate(pickedDate.getText().toString(), DateUtils.DATE_FORMAT_22, DateUtils.DATE_FORMAT_7) + "");
                    complete.setEnabled(false);
                }
            }
        });

        pickedTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(pickedTime.getText()) && pickedTime.getText().toString().length() == 0)
                    pickedDate.requestFocus();
                    map.put("perform_time",pickedTime.getText().toString());
            }
        });

        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(pickedDate.getText()) && !TextUtils.isEmpty(to.getText()) && !TextUtils.isEmpty(from.getText()) && rights.isChecked()) {
                    complete.setEnabled(true);
                } else {
                    complete.setEnabled(false);
                }
            }
        });

        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(pickedDate.getText()) && !TextUtils.isEmpty(to.getText()) && !TextUtils.isEmpty(from.getText()) && rights.isChecked()) {
                    complete.setEnabled(true);
                } else {
                    complete.setEnabled(false);
                }
            }
        });

        rights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(pickedDate.getText()) && !TextUtils.isEmpty(to.getText()) && !TextUtils.isEmpty(from.getText()) && rights.isChecked()) {
                    complete.setEnabled(true);
                } else {
                    complete.setEnabled(false);
                }
            }
        });

        rightsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rights.isChecked()) {
                    rights.setChecked(false);
                } else {
                    rights.setChecked(true);
                }

                if (!TextUtils.isEmpty(pickedDate.getText()) && !TextUtils.isEmpty(to.getText()) && !TextUtils.isEmpty(from.getText()) && rights.isChecked()) {
                    complete.setEnabled(true);
                } else {
                    complete.setEnabled(false);
                }
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete.setEnabled(false);
                swipeLayout.setRefreshing(true);
                if (map.get("perform_date") != null) {
                    if (!map.get("perform_date").matches(REG_DATA_EXPRESSION_1)) {
                        toast("Дата представлена в неправильном формате");
                        swipeLayout.setRefreshing(false);
                        return;
                    }
                } else {
                    toast("Вы не указали дату");
                    swipeLayout.setRefreshing(false);
                    return;
                }
                if (map.get("perform_time") != null && !map.get("perform_time").matches(REG_DATA_EXPRESSION_2)) {
                    toast("Время представлено в неправильном формате");
                    swipeLayout.setRefreshing(false);
                    return;
                }
                if (!TextUtils.isEmpty(from.getText())) {
                    map.put("added_from", from.getText().toString());
                } else {
                    toast("Поле \"От кого\" является обязательным для заполнения");
                    swipeLayout.setRefreshing(false);
                    return;
                }
                if (!TextUtils.isEmpty(to.getText())) {
                    map.put("comment", to.getText().toString());
                } else {
                    toast("Поле \"На кого\" является обязательным для заполнения");
                    swipeLayout.setRefreshing(false);
                    return;
                }
                createOrder();
            }
        });

        setRecentTime();
        getMyServices();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void setRecentTime(){
        String selectedDateTime = DateUtils.formatDateWithoutTimeZone(String.format("%d-%d-%d %d:%d:%d",
                calendar.get(Calendar.YEAR),
                (calendar.get(Calendar.MONTH) + 1),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), 0), DateUtils.DATE_FORMAT_5);

        String date = DateUtils.formatDate(selectedDateTime, DateUtils.DATE_FORMAT_5, DateUtils.DATE_FORMAT_22);
        String time = DateUtils.formatDate(selectedDateTime, DateUtils.DATE_FORMAT_5, DateUtils.DATE_FORMAT_14);
        pickedDate.setText(date);
        pickedTime.setText(time);
    }

    private void createOrder(){
        RestModuleNew.provideRestService().post(CREATE_ORDER, AppSettingsProvider.getInstance().getToken(AddOrder.this), map, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                swipeLayout.setRefreshing(false);
                try {
                    Intent intent = new Intent(AddOrder.this, Class.forName(classParent));
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

    public void toast(String str){
        if (str != null) {
            if(str.equalsIgnoreCase("token_invalid")){
                RestModuleNew.refreshToken(AddOrder.this, AppSettingsProvider.getInstance().getToken(AddOrder.this));
                return;
            }else
                Toast.makeText(AddOrder.this, str, Toast.LENGTH_SHORT).show();
        }
    }

    private void getMyServices(){
        RestModuleNew.provideRestService().getAuth(SERVICES, AppSettingsProvider.getInstance().getToken(AddOrder.this), null, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                swipeLayout.setRefreshing(false);
                try {
                    Services services = ObjectMap.getInstance().readValue(s, Services.class);
                    setServicesSpinner(services);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                toast(HttpErrorHandler.handleError(error));
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void setServicesSpinner(Services services){
        List<String> items = new ArrayList<>();

        for(Services.Data x : services.getData()){
               items.add(x.getTitle());
               serviceIds.add(x.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);

        serviceSpinner.setAdapter(adapter);
        swipeLayout.setRefreshing(false);
    }

    private void showDateTimeDialog(final EditText view) {
        // Create the dialog

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Dialog mDateTimeDialog = new Dialog(AddOrder.this);

        // Inflate the root layout

        final RelativeLayout mDateTimeDialogView = (RelativeLayout) inflater.inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);


        // Check is system is set to use 24h time (this doesn't seem to work as expected though)
        final String timeS = android.provider.Settings.System.getString(AddOrder.this.getContentResolver(), android.provider.Settings.System.TIME_12_24);
        final boolean is24h = !(timeS == null || timeS.equals("12"));

        // Update demo TextViews when the "OK" button is clicked
        ((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.clearFocus();

                String selectedDateTime = DateUtils.formatDateWithoutTimeZone(String.format("%d-%d-%d %d:%d:%d",
                        mDateTimePicker.get(Calendar.YEAR),
                        (mDateTimePicker.get(Calendar.MONTH) + 1),
                        mDateTimePicker.get(Calendar.DAY_OF_MONTH),
                        mDateTimePicker.get(Calendar.HOUR_OF_DAY),
                        mDateTimePicker.get(Calendar.MINUTE),
                        0), DateUtils.DATE_FORMAT_5);

                    if (DateUtils.checkDateMinToday(selectedDateTime, DateUtils.DATE_FORMAT_5)) {
                        Toast.makeText(AddOrder.this, "Невозможно создать заявку на прошедшее время\nУбедитесь, что часы и минуты выставленны правильно", Toast.LENGTH_LONG).show();
                        return;
                    }

                String date = DateUtils.formatDate(selectedDateTime, DateUtils.DATE_FORMAT_5, DateUtils.DATE_FORMAT_22);
                String date2 = DateUtils.formatDate(selectedDateTime, DateUtils.DATE_FORMAT_5, DateUtils.DATE_FORMAT_7);
                String time = DateUtils.formatDate(selectedDateTime, DateUtils.DATE_FORMAT_5, DateUtils.DATE_FORMAT_14);
                pickedDate.setText(date);
                pickedTime.setText(time);

                map.put("perform_date", date2);
                map.put("perform_time",time);

                mDateTimeDialog.dismiss();
            }
        });

        ((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimeDialog.cancel();
            }
        });

        ((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset(true, "");
            }
        });
        mDateTimePicker.setIs24HourView(is24h);
        mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDateTimeDialog.setContentView(mDateTimeDialogView);
        mDateTimeDialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        AddOrder.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return false;
    }
}
