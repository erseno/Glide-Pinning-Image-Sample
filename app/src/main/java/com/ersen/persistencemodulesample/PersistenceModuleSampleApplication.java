package com.ersen.persistencemodulesample;

public class PersistenceModuleSampleApplication extends android.app.Application{
    private static PersistenceModuleSampleApplication sInstance; //this application instance to access system wide stuff globally

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static PersistenceModuleSampleApplication getInstance() {
        return sInstance;
    }
}
