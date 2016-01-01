package com.security.village.settingsholder;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by vgaidarji on 11.02.14.
 */
public class SharedPreferencesSettingsProvider implements LocalSettingsProvider {
    private static final String PREFERENCES_NAME = "VillagePreferences";



    /**
     * Add string value to application shared preferences
     *
     * @param context Current application context
     * @param key     Preference key
     * @param value   String value
     */
    public static void addStringToPreferences(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Get string value from application shared preferences
     *
     * @param context      Current application context
     * @param key          Preference key
     * @param defaultValue Default string value
     * @return Appropriate string value
     */
    public static String getStringFromPreferences(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    @Override
    public void saveToken(Context context, String token) {
        addStringToPreferences(context, Keys.TOKEN, token);
    }

    @Override
    public String getToken(Context context) {
        return getStringFromPreferences(context, Keys.TOKEN, null);
    }

    @Override
    public void saveLogin(Context context, String login) {
        addStringToPreferences(context, Keys.LOGIN, login);
    }

    @Override
    public String getLogin(Context context) {
        return getStringFromPreferences(context, Keys.LOGIN, null);
    }

    @Override
    public void savePassword(Context context, String password) {
        addStringToPreferences(context, Keys.PASSWORD, password);
    }

    @Override
    public String getPassword(Context context) {
        return getStringFromPreferences(context, Keys.PASSWORD, null);
    }

    @Override
    public void setSdkVersion(Context context, String number) {
        addStringToPreferences(context, Keys.SDK_VERSION, number);
    }

    @Override
    public String getSdkVersion(Context context) {
        return getStringFromPreferences(context, Keys.SDK_VERSION, null);
    }
}