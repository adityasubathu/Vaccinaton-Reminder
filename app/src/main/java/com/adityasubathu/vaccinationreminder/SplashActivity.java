package com.adityasubathu.vaccinationreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static String publicUsername;
    SharedPreferences mySharedPrefs;
    String storedName;
    boolean loginFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mySharedPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        loginFlag = mySharedPrefs.getBoolean("login", false);
        storedName = mySharedPrefs.getString("username", null);
        new Thread() {

            public void run() {

                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!loginFlag) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                    if (storedName != null && !storedName.isEmpty()) {
                        publicUsername = storedName;
                    }
                } else {

                    Intent i2 = new Intent(SplashActivity.this, MainFragmentHolder.class);
                    startActivity(i2);
                    finish();
                }
            }
        }.start();
    }
}

