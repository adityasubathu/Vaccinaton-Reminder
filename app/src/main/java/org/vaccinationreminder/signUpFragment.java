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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class signUpFragment extends Fragment implements View.OnClickListener {
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

        username = loginFragment.username;
        password = loginFragment.password;

        emailSignupField = v.findViewById(R.id.emailSignupField);
        usernameSignupField = v.findViewById(R.id.usernameSignupField);
        passwordSignupField = v.findViewById(R.id.passwordSignupField);
        fullNameSignupField = v.findViewById(R.id.fullNameSignupField);
        loginLauncherTextView = v.findViewById(R.id.loginLauncherTextView);

        loginLauncherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragment loginFragmentObject = new loginFragment();
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.loginActivityFragmentHolder, loginFragmentObject, "loginFragment");
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

        usernameSignupField.setText(loginFragment.username);
        passwordSignupField.setText(loginFragment.password);

        signupButton = v.findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        email = emailSignupField.getText().toString();
        fullName = fullNameSignupField.getText().toString();

        mySharedPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String storedEmail = mySharedPrefs.getString("email", null);
        String storedUsername = mySharedPrefs.getString("username", null);

        if (email.equals(storedEmail)) {

            Toast.makeText(getActivity(), "Email already registered", Toast.LENGTH_SHORT).show();

            emailSignupField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

            emailSignupField.getBackground().setColorFilter(getResources().getColor(R.color.error_red), PorterDuff.Mode.SRC_ATOP);

            emailSignupField.setTextColor(getResources().getColor(R.color.error_red));

        } else if (email.isEmpty()) {

            emailSignupField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

            emailSignupField.getBackground().setColorFilter(getResources().getColor(R.color.error_red), PorterDuff.Mode.SRC_ATOP);
            emailSignupField.setHintTextColor(getResources().getColor(R.color.error_red));

            //Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();

        } else if (username.isEmpty()) {

            usernameSignupField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

            usernameSignupField.getBackground().setColorFilter(getResources().getColor(R.color.error_red), PorterDuff.Mode.SRC_ATOP);
            usernameSignupField.setHintTextColor(getResources().getColor(R.color.error_red));

            //Toast.makeText(getActivity(), "Please enter a username", Toast.LENGTH_SHORT).show();

        } else if (username.equals(storedUsername)) {

            Toast.makeText(getActivity(), "Username already registered", Toast.LENGTH_SHORT).show();

            usernameSignupField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

            usernameSignupField.getBackground().setColorFilter(getResources().getColor(R.color.error_red), PorterDuff.Mode.SRC_ATOP);

            usernameSignupField.setTextColor(getResources().getColor(R.color.error_red));


        } else if (fullName.isEmpty()) {

            fullNameSignupField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

            fullNameSignupField.getBackground().setColorFilter(getResources().getColor(R.color.error_red), PorterDuff.Mode.SRC_ATOP);
            fullNameSignupField.setHintTextColor(getResources().getColor(R.color.error_red));

            Toast.makeText(getActivity(), "Please enter your Name", Toast.LENGTH_SHORT).show();

        } else if (password.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter a password", Toast.LENGTH_SHORT).show();
            passwordSignupField.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);

            passwordSignupField.getBackground().setColorFilter(getResources().getColor(R.color.error_red), PorterDuff.Mode.SRC_ATOP);
            passwordSignupField.setHintTextColor(getResources().getColor(R.color.error_red));
            
        } else {

            SharedPreferences.Editor e = mySharedPrefs.edit();

            e.putString("username", username);
            e.putString("password", password);
            e.putString("email", email);
            e.putString("fullName", fullName);

            e.apply();

            Intent i = new Intent(getActivity(), MainFragmentHolder.class);
            startActivity(i);
            getActivity().finish();
        }

    }
}
