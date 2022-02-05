package com.dktechhub.chatlockerforwhatsapp;


import androidx.multidex.MultiDexApplication;

//import androidx.lifecycle.Process
//import com.google.android.gms.ads.MobileAds;
//import com.vestaentertainment.whatsappchatloker.ads.AppSettings;

public class MyApplication extends MultiDexApplication {
    public static boolean isAPPBackground = false;

    public void onCreate() {
        //ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        super.onCreate();

        //MobileAds.initialize(getApplicationContext(), AppSettings.pubId);
    }

}