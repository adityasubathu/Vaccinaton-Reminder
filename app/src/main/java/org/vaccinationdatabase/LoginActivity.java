package org.vaccinationdatabase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    static String email, password, username, fullName;
    EditText usernameField, passwordField, emailSignupField, usernameSignupField, passwordSignupField, fullNameSignupField;
    Button loginButton, signupButton;
    SharedPreferences mySharedPrefs;
    View signupDialog;

    @SuppressLint("InflateParams")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupDialog = getLayoutInflater().inflate(R.layout.layout_signup_dialog, null);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);

        usernameSignupField = signupDialog.findViewById(R.id.usernameSignupField);
        passwordSignupField = signupDialog.findViewById(R.id.passwordSignupField);
        emailSignupField = signupDialog.findViewById(R.id.emailSignupField);
        fullNameSignupField = signupDialog.findViewById(R.id.fullNameSignupField);

        loginButton = findViewById(R.id.loginButton);
        signupButton = signupDialog.findViewById(R.id.signupButton);
    }


    public void loginMethod(View view) {

        username = usernameField.getText().toString();
        password = passwordField.getText().toString();

        mySharedPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);

        String storedName = mySharedPrefs.getString("username", null);
        String storedPassword = mySharedPrefs.getString("password", null);

        if (password.isEmpty() || username.isEmpty()) {

            Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show();

        } else if (username.equals(storedName)) {

            if (password.isEmpty()) {

                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();

            } else if (password.equals(storedPassword)) {

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
            }
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            if (signupDialog.getParent() != null) {

                ((ViewGroup) signupDialog.getParent()).removeView(signupDialog);
            }

            usernameSignupField.setText(username);
            passwordSignupField.setText(password);
            fullNameSignupField.requestFocus();

            alertDialog.setView(signupDialog);

            alertDialog.show();
        }
    }

    public void signupMethod(View v) {

        username = usernameField.getText().toString();
        password = passwordField.getText().toString();
        email = emailSignupField.getText().toString();
        fullName = fullNameSignupField.getText().toString();

        mySharedPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);

        String storedEmail = mySharedPrefs.getString("email", null);

        if (email.equals(storedEmail)) {

            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();

        } else {

            SharedPreferences.Editor e = mySharedPrefs.edit();

            e.putString("username", username);
            e.putString("password", password);
            e.putString("email", email);
            e.putString("fullName", fullName);

            e.apply();

            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }
}

