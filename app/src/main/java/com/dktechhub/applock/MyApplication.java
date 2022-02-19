package com.dktechhub.applock;


import androidx.multidex.MultiDexApplication;

import com.dktechhub.applock.utils.AppDatabase;


public class MyApplication extends MultiDexApplication {
    public static boolean isAPPBackground = false;

    public void onCreate() {
        //ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        super.onCreate();
        AppDatabase.initialize(this);
        AppDatabase.getInstance().loadAll();
        //MobileAds.initialize(getApplicationContext(), AppSettings.pubId);
    }

}