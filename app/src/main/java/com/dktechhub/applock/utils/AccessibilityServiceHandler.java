package com.dktechhub.applock.utils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class AccessibilityServiceHandler extends AccessibilityService {
    Context context;
    public String currentAccessibilityPackage;
    private HashSet<String> escaped = new HashSet<>();
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
                if(escaped.contains(s)||s.contains("systemui")||s.contains("inputmethod"))
                    return;
                if(s.equals(currentAccessibilityPackage))
                    return;
                this.currentAccessibilityPackage = s;

                Toast.makeText(this, "Window changed :"+currentAccessibilityPackage, Toast.LENGTH_SHORT).show();
                if(AppDatabase.allApps.containsKey(currentAccessibilityPackage)&&AppDatabase.allApps.get(currentAccessibilityPackage).second==1)
                {
                    Toast.makeText(this, "locked app", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context,LockActivity.class).putExtra("fromService","yes").addFlags(335577088));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        escaped.add(getPackageName().toLowerCase());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service destroyed..will be started again", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent("onServiceDestroyed"));
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Toast.makeText(this, "service destroyed..will be started again", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent("onServiceDestroyed"));
        Intent i = new Intent(getApplicationContext(),this.getClass());
        i.setPackage(getPackageName());
        PendingIntent pi = PendingIntent.getService(getApplicationContext(),1,i,PendingIntent.FLAG_ONE_SHOT);
        ((AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+1000,pi);
        super.onTaskRemoved(rootIntent);
    }

    public void onInterrupt() {
        Log.e("TAG", "onInterrupt");
    }
}