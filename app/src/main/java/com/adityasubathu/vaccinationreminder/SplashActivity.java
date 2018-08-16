package com.adityasubathu.vaccinationreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static String publicUsername;
    SharedPreferences mySharedPrefs, loginFlagPrefs;
    String activeUser;
    boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginFlagPrefs = getSharedPreferences("loginInfo", MODE_PRIVATE);
        activeUser = loginFlagPrefs.getString("activeUser", null);
        loggedIn = loginFlagPrefs.getBoolean("loggedIn", false);

        new Thread() {

            public void run() {

                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!loggedIn) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                    if (activeUser != null && !activeUser.isEmpty()) {
                        publicUsername = activeUser;
                    }
                } else {
                    LoginActivitySignUpFragment.activeUsername = activeUser;
                    Intent i2 = new Intent(SplashActivity.this, MainFragmentHolder.class);
                    startActivity(i2);
                    finish();
                }
            }
        }.start();
    }
}

