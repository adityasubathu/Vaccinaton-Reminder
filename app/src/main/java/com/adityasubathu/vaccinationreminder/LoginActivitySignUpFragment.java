package com.adityasubathu.vaccinationreminder;

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
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class LoginActivitySignUpFragment extends Fragment implements View.OnClickListener {
    static String email, password, username, fullName;
    EditText emailSignupField, usernameSignupField, passwordSignupField, fullNameSignupField;
    SharedPreferences mySharedPrefs;
    TextView loginLauncherTextView;
    View v;
    Button signupButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.sign_up_fragment, container, false);
        return v;
    }

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

        usernameSignupField.setText(LoginActivityLoginFragment.username);
        passwordSignupField.setText(LoginActivityLoginFragment.password);

        signupButton = v.findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        email = emailSignupField.getText().toString();
        fullName = fullNameSignupField.getText().toString();
        username = usernameSignupField.getText().toString();
        password = passwordSignupField.getText().toString();

        mySharedPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String storedEmail = mySharedPrefs.getString("email", null);
        String storedUsername = mySharedPrefs.getString("username", null);

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty() || email.equals(storedEmail) || username.equals(storedUsername)) {

            if (fullName.isEmpty()) {
                changeColorToRed(fullNameSignupField);
                fullNameSignupField.setError("Please enter your Name");
            }
            if (password.isEmpty()) {
                changeColorToRed(passwordSignupField);
                passwordSignupField.setError("Please enter a password");
            }
            if (email.isEmpty()) {
                changeColorToRed(emailSignupField);
                emailSignupField.setError("Please enter your email");
            }
            if (username.isEmpty()) {
                changeColorToRed(usernameSignupField);
                usernameSignupField.setError("Please enter a username");
            }
            if (email.equals(storedEmail)) {
                changeColorToRed(emailSignupField);
                emailSignupField.setError("Email already registered");
            }
            if (username.equals(storedUsername)) {
                changeColorToRed(usernameSignupField);
                usernameSignupField.setError("Username already registered");
            }

        } else {

            SharedPreferences.Editor e = mySharedPrefs.edit();

            e.putString("username", username);
            e.putString("password", password);
            e.putString("email", email);
            e.putString("fullName", fullName);
            e.putBoolean("login", true);

            e.apply();

            Intent i = new Intent(getActivity(), MainFragmentHolder.class);
            startActivity(i);
            getActivity().finish();
        }

    }

    private void changeColorToRed(@NonNull final EditText field) {

        field.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

        field.getBackground().setColorFilter(getResources().getColor(R.color.error_red), PorterDuff.Mode.SRC_ATOP);

        field.setHintTextColor(getResources().getColor(R.color.error_red));

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
}
