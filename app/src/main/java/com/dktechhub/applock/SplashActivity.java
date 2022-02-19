package com.dktechhub.applock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dktechhub.applock.utils.LockActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
             SplashActivity.this.startActivity(new Intent(SplashActivity.this, LockActivity.class));
             SplashActivity.this.finish();
         }, 3000);

    }
}