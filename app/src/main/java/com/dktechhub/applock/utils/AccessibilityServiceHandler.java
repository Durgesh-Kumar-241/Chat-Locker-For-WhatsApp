package com.dktechhub.applock.utils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.ArrayList;

public class AccessibilityServiceHandler extends AccessibilityService {
    Context context;
    public String currentAccessibilityPackage;
    public void onServiceConnected() {
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return START_STICKY;
    }

    @SuppressLint({"WrongConstant"})
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent != null && accessibilityEvent.getPackageName() != null) {
            try {
                String s = accessibilityEvent.getPackageName().toString().toLowerCase();
                if(s.equals(currentAccessibilityPackage))
                    return;
                this.currentAccessibilityPackage = s;

                Toast.makeText(this, "Window changed :"+currentAccessibilityPackage, Toast.LENGTH_SHORT).show();
                if(AppDatabase.allApps.containsKey(currentAccessibilityPackage)&&AppDatabase.allApps.get(currentAccessibilityPackage).second==1)
                {
                    Toast.makeText(this, "locked app", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,LockActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }


    public void onInterrupt() {
        Log.e("TAG", "onInterrupt");
    }
}