package org.vaccinationdatabase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences mySharedPrefs;
    String storedName, storedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mySharedPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);

        storedName = mySharedPrefs.getString("username", null);
        storedPassword = mySharedPrefs.getString("password", null);

        new Thread() {

            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (storedName != null && storedPassword != null) {

                    if (storedName.isEmpty() || storedPassword.isEmpty()) {

                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {

                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }

                } else {

                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }.start();
    }
}

