package org.vaccinationreminder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginActivityLoginFragment loginFragment = new LoginActivityLoginFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.loginActivityFragmentHolder, loginFragment, "LoginActivityLoginFragment");
        ft.commit();

    }
}