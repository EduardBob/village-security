package com.security.village.settingsholder;

public class AppSettingsProvider {
    public static class SingletonHolder {
        public static final LocalSettingsProvider HOLDER_INSTANCE = new SharedPreferencesSettingsProvider();
    }

    public synchronized static LocalSettingsProvider getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }
}
