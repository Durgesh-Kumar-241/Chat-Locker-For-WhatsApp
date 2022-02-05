package com.dktechhub.chatlockerforwhatsapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.accessibilityservice.AccessibilityService;
public class RebootReciver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            context.startService(new Intent(context, AccessibilityService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}