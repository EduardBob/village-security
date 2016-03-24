package com.security.village.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.security.village.HttpErrorHandler;
import com.security.village.ObjectMap;
import com.security.village.R;
import com.security.village.settingsholder.AppSettingsProvider;
import com.security.village.webservice.retrofit.RestModuleNew;
import com.security.village.webservice.retrofit.response.Profile;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by fwmobile on 3/10/16.
 */
public class MyProfile extends Activity {

    public static final String ME_GET = "api/v1/me";

    private TextView name;
    private TextView phone;
    private TextView country;
    private TextView logOut;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);

        back = (ImageView) findViewById(R.id.left_button);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        country = (TextView) findViewById(R.id.poselok);
        logOut = (TextView) findViewById(R.id.log_out);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSettingsProvider.getInstance().saveToken(getApplicationContext(), null);
                AppSettingsProvider.getInstance().savePassword(getApplicationContext(), null);
                AppSettingsProvider.getInstance().saveLogin(getApplicationContext(), null);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getProfile();
    }

    private void getProfile() {
        RestModuleNew.provideRestService().getAuth(ME_GET, AppSettingsProvider.getInstance().getToken(MyProfile.this), null, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                try {
                    Profile profile = ObjectMap.getInstance().readValue(s, Profile.class);
                    setProfile(profile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                toast(HttpErrorHandler.handleError(error), AppSettingsProvider.getInstance().getToken(MyProfile.this));
            }
        });
    }

    public void toast(String str, String token) {
        if (str != null) {
            if (str.equalsIgnoreCase("token_invalid")) {
                RestModuleNew.refreshToken(MyProfile.this, token);
                return;
            } else
                Toast.makeText(MyProfile.this, str, Toast.LENGTH_SHORT).show();
        }
    }

    private void setProfile(Profile profile) {

        try {
            String s = "";
            if (profile.getData().getFirst_name() != null)
                s += profile.getData().getFirst_name();
            if (profile.getData().getLast_name() != null)
                s += " " + profile.getData().getLast_name();
            name.setText(s);
        } catch (Exception e) {
            e.printStackTrace();

        }

        try{
            phone.setText(profile.getData().getPhone());
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            String s = "";
            if(profile.getData().getBuilding().getData().getVillage().getData().getName() != null)
                s += profile.getData().getBuilding().getData().getVillage().getData().getName();
            if(profile.getData().getBuilding().getData().getAddress() != null)
                s += ", " + profile.getData().getBuilding().getData().getAddress();
            country.setText(s);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
