package org.vaccinationreminder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView welcomeNameField;
    String[] arr;
    String firstName;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeNameField = findViewById(R.id.nameTextView);

        SharedPreferences myPrefs = getSharedPreferences("userInfo", MODE_PRIVATE);

        String name = myPrefs.getString("fullName", null);

        if (name != null) {

            arr = name.split(" ");

        }

        if (arr != null) {

            firstName = arr[0];
            welcomeNameField.setText(firstName);

        }



    }
}
