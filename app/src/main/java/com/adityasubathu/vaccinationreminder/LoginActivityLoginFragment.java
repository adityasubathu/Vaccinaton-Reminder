package com.adityasubathu.vaccinationreminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class LoginActivityLoginFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    static String email, password, username, storedUsername, storedPassword;
    EditText usernameField, passwordField;
    TextView signUpLauncherTextView;
    Button loginButton;
    SharedPreferences mySharedPrefs, loginFlagPrefs;
    View v;
    ImageView passwordVisible;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.login_fragment, container, false);
        return v;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usernameField = v.findViewById(R.id.usernameField);
        passwordField = v.findViewById(R.id.passwordField);
        passwordVisible = v.findViewById(R.id.password_login_visibility);

        passwordVisible.setOnTouchListener(this);

        usernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                usernameField.setCompoundDrawables(null, null, null, null);

                usernameField.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                passwordField.setCompoundDrawables(null, null, null, null);

                passwordField.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginButton = v.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        signUpLauncherTextView = v.findViewById(R.id.signUpLauncherTextView);
        signUpLauncherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

                LoginActivitySignUpFragment signUpFragment = new LoginActivitySignUpFragment();
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.loginActivityFragmentHolder, signUpFragment, "LoginActivitySignUpFragment");
                ft.commit();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        if (SplashActivity.publicUsername != null && !SplashActivity.publicUsername.isEmpty()) {

            usernameField.setText(SplashActivity.publicUsername);
            passwordField.requestFocus();
        } else {

            usernameField.requestFocus();
        }

    }

    @Override
    public void onClick(View v) {

        username = usernameField.getText().toString();
        password = passwordField.getText().toString();

        mySharedPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences(username, Context.MODE_PRIVATE);
        loginFlagPrefs = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);

        storedUsername = mySharedPrefs.getString("username", null);
        storedPassword = mySharedPrefs.getString("password", null);

        if (username.isEmpty() || password.isEmpty() || !username.equals(storedUsername)) {

            if (username.isEmpty()) {
                changeColorToRed(usernameField, "Please Enter Your Username");
            }

            if (password.isEmpty()) {
                changeColorToRed(passwordField, "Password Cannot Be Empty");
            }

            if (!username.equals(storedUsername)) {
                changeColorToRed(usernameField, "Username Not Registered");
            }

        } else if (username.equals(storedUsername) && !password.equals(storedPassword)) {
            changeColorToRed(passwordField, "Password is Incorrect");
        } else if (username.equals(storedUsername) && password.equals(storedPassword)) {

            SharedPreferences.Editor e = loginFlagPrefs.edit();
            e.putString("activeUser", username);
            LoginActivitySignUpFragment.activeUsername = username;
            e.putBoolean("loggedIn", true);
            e.apply();

            Intent i = new Intent(getActivity(), MainFragmentHolder.class);
            startActivity(i);
            getActivity().finish();
        }

    }

    private void changeColorToRed(@NonNull final EditText field, String errorMessage) {

        //field.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);
        field.getBackground().setColorFilter(getResources().getColor(R.color.error_red), PorterDuff.Mode.SRC_ATOP);
        field.setHintTextColor(getResources().getColor(R.color.error_red));

        field.setError(errorMessage);
        field.requestFocus();

        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                field.setCompoundDrawables(null, null, null, null);

                field.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

                field.setHintTextColor(getResources().getColor(R.color.textColorBlack));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                passwordField.setTransformationMethod(null);
                passwordField.setSelection(passwordField.getText().length());
                return true;
            }

            case MotionEvent.ACTION_UP: {
                passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passwordField.setSelection(passwordField.getText().length());
                return false;
            }

        }
        return true;
    }
}
