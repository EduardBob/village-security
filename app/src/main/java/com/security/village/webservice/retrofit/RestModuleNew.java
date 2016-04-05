package com.security.village.webservice.retrofit;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.widget.Toast;

import com.security.village.HttpErrorHandler;
import com.security.village.ObjectMap;
import com.security.village.activity.LoginActivity;
import com.security.village.settingsholder.AppSettingsProvider;
import com.security.village.settingsholder.LocalSettingsProvider;
import com.security.village.webservice.retrofit.response.Token;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class RestModuleNew {
    private static RestAdapter restAdapter;

    public static void refreshToken(final Context context, final String token){
        RestModuleNew.provideRestService().getAuth(ApiNew.REFRESH_TOKEN, token, null, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                try {
                    AppSettingsProvider.getInstance().saveToken(context, "Bearer  " + ObjectMap.getInstance().readValue(s, Token.class).getToken());
                    Toast.makeText(context, "refreshed token", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getBody().toString().contains("token_invalid") || error.getBody().toString().contains("token_expired") || error.getBody().toString().contains("token_not_provided")){
                    RestModuleNew.login(context);
                }

                Toast.makeText(context, error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void login(final Context context){
        final HashMap<String,String> map = new HashMap();
        final LocalSettingsProvider settingsHolder = AppSettingsProvider.getInstance();
        map.put("phone", settingsHolder.getLogin(context));
        map.put("password", settingsHolder.getPassword(context));
        RestModuleNew.provideRestService().post(ApiNew.LOGIN_URL, null, map, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                try {
                    settingsHolder.saveToken(context, "Bearer  " + ObjectMap.getInstance().readValue(s, Token.class).getToken());
                    settingsHolder.saveLogin(context, map.get("phone"));
                    settingsHolder.savePassword(context, map.get("password"));
                    Toast.makeText(context, "re login (end)", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
//                Toast.makeText(context, HttpErrorHandler.handleError(error), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Пожалуйста авторизируйтесь заново", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LoginActivity.class);
                if (Build.VERSION.SDK_INT > 10) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                } else {
                    intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                }
                context.startActivity(intent);
            }
        });
    }

    public static HttpInterface provideRestService() {
        if (restAdapter == null){
            synchronized (RestAdapter.class){
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
                okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ApiNew.SERVER_URL)
                        .setLogLevel(RestAdapter.LogLevel.FULL)//HEADERS_AND_ARGS
                        .setLog(new AndroidLog("Retrofit_Village"))
                        .setClient(new OkClient(okHttpClient))
                        .setConverter(new Converter())
                        .build();
                return restAdapter.create(HttpInterface.class);
            }
        } else {
            return  restAdapter.create(HttpInterface.class);
        }
    }

}
