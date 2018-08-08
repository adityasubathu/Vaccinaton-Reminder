package org.vaccinationreminder;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class loginFragment extends Fragment implements View.OnClickListener {

    static String email, password, username, storedUsername, storedPassword;
    EditText usernameField, passwordField;
    TextView signUpLauncherTextView;
    Button loginButton;
    SharedPreferences mySharedPrefs;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.login_fragment, container, false);

        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usernameField = v.findViewById(R.id.usernameField);
        passwordField = v.findViewById(R.id.passwordField);

        usernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                usernameField.setCompoundDrawables(null, null, null, null);

                usernameField.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP);
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

                passwordField.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP);

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

                signUpFragment signUpFragment = new signUpFragment();
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.loginActivityFragmentHolder, signUpFragment, "signUpFragment");
                ft.commit();
            }
        });

    }

    @Override
    public void onClick(View v) {

        username = usernameField.getText().toString();
        password = passwordField.getText().toString();

        mySharedPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        storedUsername = mySharedPrefs.getString("username", null);
        storedPassword = mySharedPrefs.getString("password", null);

        if (username.isEmpty()) {

            Toast.makeText(getActivity(), "Username Empty", Toast.LENGTH_SHORT).show();

            usernameField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

            usernameField.getBackground().setColorFilter(getResources().getColor(R.color.error_red),
                    PorterDuff.Mode.SRC_ATOP);
            usernameField.setHintTextColor(getResources().getColor(R.color.error_red));

            if (password.isEmpty()) {

                Toast.makeText(getActivity(), "Username & Password Empty", Toast.LENGTH_SHORT).show();

                passwordField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

                passwordField.getBackground().setColorFilter(getResources().getColor(R.color.error_red),
                        PorterDuff.Mode.SRC_ATOP);
                passwordField.setHintTextColor(getResources().getColor(R.color.error_red));

            }

        } else if (password.isEmpty()) {

            Toast.makeText(getActivity(), "Password Empty", Toast.LENGTH_SHORT).show();

            passwordField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

            passwordField.getBackground().setColorFilter(getResources().getColor(R.color.error_red),
                    PorterDuff.Mode.SRC_ATOP);
            passwordField.setHintTextColor(getResources().getColor(R.color.error_red));


        } else if (username.equals(storedUsername)) {

            if (password.isEmpty()) {

                Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();

            } else if (password.equals(storedPassword)) {

                Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), MainFragmentHolder.class);
                startActivity(i);
                getActivity().finish();

            } else {
                Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(getActivity(), "Username Not Registered. Sign Up Now", Toast.LENGTH_SHORT).show();

            usernameField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

            usernameField.getBackground().setColorFilter(getResources().getColor(R.color.error_red),
                    PorterDuff.Mode.SRC_ATOP);

            usernameField.setTextColor(getResources().getColor(R.color.error_red));
            passwordField.setTextColor(getResources().getColor(R.color.error_red));
        }

    }
}
