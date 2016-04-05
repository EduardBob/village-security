package com.security.village.settingsholder;

import android.content.Context;

/**
 * Created by vgaidarji on 18.04.2014.
 */
public interface LocalSettingsProvider {

    void saveToken(Context context, String token);
    String getToken(Context context);

    void saveLogin(Context context, String login);
    String getLogin(Context context);

    void savePassword(Context context, String password);
    String getPassword(Context context);

    void setSdkVersion(Context context, String number);
    String getSdkVersion(Context context);

    void saveRefreshListTime(Context context, int number);
    int getRefreshListTime(Context context);
}
