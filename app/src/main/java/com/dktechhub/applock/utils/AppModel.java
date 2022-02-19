package com.dktechhub.applock.utils;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class AppModel {
    public int isLocked;
    public String packageName;
    public String appName;
    public Drawable  icon;
    public int id=-1;
    public AppModel(String appName, String packageName, Drawable drawable)
    {
        this.packageName=packageName;
        isLocked=0;
        this.appName=appName;
        this.icon = drawable;
    }
    public AppModel(String appName,String packageName,Drawable drawable,int id,int isLocked) {
        this(appName,packageName,drawable);
        this.isLocked = isLocked;
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return appName+isLocked+"id:"+id;
    }

    public int isLocked() {
        return isLocked;
    }

    public void setLocked(int locked) {
        isLocked = locked;
    }
}
