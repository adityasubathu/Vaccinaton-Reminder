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

public class LoginActivitySignUpFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    static String email, password, username, fullName;
    public static String activeUsername;
    EditText emailSignupField, usernameSignupField, passwordSignupField, passwordConfirmSignupField, fullNameSignupField;
    SharedPreferences mySharedPrefs, loginFlagPrefs;
    TextView loginLauncherTextView;
    View v;
    Button signupButton;
    ImageView passwordVisible, passwordConfirmVisible;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.sign_up_fragment, container, false);
        return v;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart() {
        super.onStart();

        username = LoginActivityLoginFragment.username;
        password = LoginActivityLoginFragment.password;

        emailSignupField = v.findViewById(R.id.emailSignupField);
        usernameSignupField = v.findViewById(R.id.usernameSignupField);
        passwordSignupField = v.findViewById(R.id.passwordSignupField);
        fullNameSignupField = v.findViewById(R.id.fullNameSignupField);
        loginLauncherTextView = v.findViewById(R.id.loginLauncherTextView);
        passwordConfirmSignupField = v.findViewById(R.id.passwordConfirmSignupField);
        passwordVisible = v.findViewById(R.id.password_signup_visibility);
        passwordConfirmVisible = v.findViewById(R.id.password_signup_confirm_visibility);

        loginLauncherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivityLoginFragment loginFragmentObject = new LoginActivityLoginFragment();
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.loginActivityFragmentHolder, loginFragmentObject, "LoginActivityLoginFragment");
                ft.commit();
            }
        });

        if (username != null && !username.isEmpty()) {
            usernameSignupField.setText(username);
        }
        if (password != null && !password.isEmpty()) {
            passwordSignupField.setText(password);
        }

        fullNameSignupField.requestFocus();

        signupButton = v.findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);

        passwordVisible.setOnTouchListener(this);
        passwordConfirmVisible.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {

        email = emailSignupField.getText().toString();
        fullName = fullNameSignupField.getText().toString();
        username = usernameSignupField.getText().toString();
        password = passwordSignupField.getText().toString();
        String confirmPassword = passwordConfirmSignupField.getText().toString();

        mySharedPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences(username, Context.MODE_PRIVATE);
        loginFlagPrefs = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);

        String storedEmail = mySharedPrefs.getString("email", null);
        String storedUsername = mySharedPrefs.getString("username", null);

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty() || email.equals(storedEmail) || username.equals(storedUsername) || !password.equals(confirmPassword)) {

            if (fullName.isEmpty()) {
                changeColorToRed(fullNameSignupField, "Please Enter Your Name");
            }
            if (password.isEmpty()) {
                changeColorToRed(passwordSignupField, "Please Enter a Password");
            }
            if (email.isEmpty()) {
                changeColorToRed(emailSignupField, "Please Enter Your Email");
            }
            if (username.isEmpty()) {
                changeColorToRed(usernameSignupField, "Please Enter a Username");
            }
            if (email.equals(storedEmail)) {
                changeColorToRed(emailSignupField, "Email Already Registered");
            }
            if (username.equals(storedUsername)) {
                changeColorToRed(usernameSignupField, "Username Already Registered");
            }
            if (!password.equals(confirmPassword)) {
                changeColorToRed(passwordSignupField, "Passwords Do Not Match");
                changeColorToRed(passwordConfirmSignupField, "Passwords Do Not Match");
            }

        } else {

            SharedPreferences.Editor e = mySharedPrefs.edit();
            SharedPreferences.Editor e1 = loginFlagPrefs.edit();

            e.putString("username", username);
            e.putString("password", password);
            e.putString("email", email);
            e.putString("fullName", fullName);
            e1.putBoolean("loggedIn", true);
            e1.putString("activeUser", username);
            activeUsername = username;
            e.apply();
            e1.apply();
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

        int id = view.getId();

        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN: {

                if (id == R.id.password_signup_visibility) {

                    passwordSignupField.setTransformationMethod(null);
                    passwordSignupField.setSelection(passwordSignupField.getText().length());

                } else if (id == R.id.password_signup_confirm_visibility) {

                    passwordConfirmSignupField.setTransformationMethod(null);
                    passwordConfirmSignupField.setSelection(passwordConfirmSignupField.getText().length());
                }
                return true;
            }

            case MotionEvent.ACTION_UP: {

                if (id == R.id.password_signup_visibility) {

                    passwordSignupField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordSignupField.setSelection(passwordSignupField.getText().length());

                } else if (id == R.id.password_signup_confirm_visibility) {

                    passwordConfirmSignupField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordConfirmSignupField.setSelection(passwordConfirmSignupField.getText().length());
                }
                return false;
            }
        }

        return true;
    }
}
