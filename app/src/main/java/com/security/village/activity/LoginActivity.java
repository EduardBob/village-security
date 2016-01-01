package com.security.village.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.security.village.ObjectMap;
import com.security.village.R;
import com.security.village.settingsholder.AppSettingsProvider;
import com.security.village.settingsholder.Keys;
import com.security.village.settingsholder.LocalSettingsProvider;
import com.security.village.webservice.retrofit.ApiNew;
import com.security.village.webservice.retrofit.RestModuleNew;
import com.security.village.webservice.retrofit.response.Token;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by fruitware on 12/23/15.
 */
public class LoginActivity extends Activity {
    private EditText login1;
    private EditText login2;
    private EditText password;
    private TextView buttonLogin;
    private SwipeRefreshLayout swipeLayout;

    private LinearLayout loginLayout;
    private LinearLayout passwordLayout;

    private HashMap<String, String> map;

    private String loginString = null;

    private LocalSettingsProvider settingsHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        map = new HashMap<>();
        settingsHolder = AppSettingsProvider.getInstance();

        AppSettingsProvider.getInstance().setSdkVersion(LoginActivity.this, Build.VERSION.SDK);

        initializeAllViews();
        swipeLayout.setRefreshing(true);
        isLoggedIn();
    }

    private void isLoggedIn(){
        swipeLayout.setRefreshing(true);
        setState(false);
        String loginString = settingsHolder.getLogin(LoginActivity.this);
        String password = settingsHolder.getPassword(LoginActivity.this);
        if(loginString != null && password != null){
            map.put("phone", loginString);
            map.put("password", password);
            login();
        }else{
            setState(true);
            swipeLayout.setRefreshing(false);
        }
    }

    private void initializeAllViews(){
        login1 = (EditText) findViewById(R.id.login_phone_1);
        login2 = (EditText) findViewById(R.id.login_phone_2);
        password = (EditText) findViewById(R.id.password);
        buttonLogin = (TextView) findViewById(R.id.login_button);
        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        passwordLayout = (LinearLayout) findViewById(R.id.password_layout);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setEnabled(false);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeLayout.setRefreshing(true);
                if(checkLoginPassword()){
                    swipeLayout.setRefreshing(true);
                    setState(false);
                    login();
                }
            }
        });

        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(login1.getText()) || login1.getText().toString().length() < 3){
                    login1.requestFocus();
                } else if(TextUtils.isEmpty(login2.getText()) || login2.getText().toString().length() < 7){
                    login2.requestFocus();
                }
            }
        });

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(login1.getText()) || login1.getText().toString().length() < 3){
                    login1.requestFocus();
                }
            }
        });

        passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.requestFocus();
            }
        });

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(login1.getText()) || login1.getText().toString().length() < 3){
                    login1.requestFocus();
                } else if(TextUtils.isEmpty(login2.getText()) || login2.getText().toString().length() < 7){
                    login2.requestFocus();
                }
            }
        });

        login1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 3) {
                    login2.requestFocus();
                }
            }
        });

        login2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if((TextUtils.isEmpty(login1.getText()) || login1.getText().toString().length() == 3) && TextUtils.isEmpty(login2.getText())){
                    login1.requestFocus();
                }
                if (s.length() == 7){
                    password.requestFocus();
                }
            }
        });
    }

    private boolean checkLoginPassword(){
        loginString = "+7(";
        if(!TextUtils.isEmpty(login1.getText()) && login1.getText().toString().length() == 3){
            loginString += login1.getText().toString()+")";
        }else{
            toast("Вы не полность ввели номер телефона");
            return false;
        }

        if(!TextUtils.isEmpty(login2.getText()) && login2.getText().toString().length() == 7){
            loginString += login2.getText().toString();
        }else{
            toast("Вы не полность ввели номер телефона");
            return false;
        }

        if(TextUtils.isEmpty(password.getText()) ){
            toast("Вы не ввели пароль");
            return false;
        }

        if(password.getText().toString().length() < 6){
            toast("Пароль должен содержать не менее 6 символов");
            return false;
        }


        map.put("phone", loginString);
        map.put("password", password.getText().toString());

        return true;
    }

    public void toast(String str){
        Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    private void setState(boolean state){
        login1.setEnabled(state);
        login2.setEnabled(state);
        password.setEnabled(state);
        buttonLogin.setEnabled(state);
    }

    private void login(){
        RestModuleNew.provideRestService().post(ApiNew.LOGIN_URL, null, map, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                setState(true);
                try {
                    settingsHolder.saveToken(LoginActivity.this, "Bearer  " + ObjectMap.getInstance().readValue(s, Token.class).getToken());
                    settingsHolder.saveLogin(LoginActivity.this, map.get("phone"));
                    settingsHolder.savePassword(LoginActivity.this, map.get("password"));
                    Intent intent = new Intent(LoginActivity.this, ActiveOrders.class);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                    swipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setState(true);
                swipeLayout.setRefreshing(false);
                try {
                    if (error.getResponse().getStatus() >= 400 && error.getResponse().getStatus() < 500) {
                        toast("Вы ввели неправильные данные");
                        return;
                    }

                    if (error.getResponse().getStatus() >= 500) {
                        toast("Приносим извинения, но на текущий момент ведутся технические работы");
                        return;
                    }
                    toast("Отсутствует соединение с интернетом");
                } catch (Exception e) {
                    e.printStackTrace();
                    toast("Отсутствует соединение с интернетом");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
