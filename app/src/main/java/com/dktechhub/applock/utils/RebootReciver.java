package com.dktechhub.applock.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.accessibilityservice.AccessibilityService;
import android.widget.Toast;

public class RebootReciver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
            context.startService(new Intent(context, AccessibilityService.class));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}