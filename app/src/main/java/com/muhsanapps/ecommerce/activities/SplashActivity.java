package com.muhsanapps.ecommerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.muhsanapps.ecommerce.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SystemClock.sleep(3000);
        Intent registerIntent= new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }
}