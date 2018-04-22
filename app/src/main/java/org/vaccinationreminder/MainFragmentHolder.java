package org.vaccinationreminder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

public class MainFragmentHolder extends AppCompatActivity {

    TextView welcomeNameField;
    String[] arr;
    String firstName;

    DrawerLayout HomeActivityNavDrawer;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);

        Toolbar toolbar = findViewById(R.id.activity_home_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        Objects.requireNonNull(actionbar).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(actionbar).setHomeAsUpIndicator(R.drawable.ic_menu);



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

        HomeActivityNavDrawer = findViewById(R.id.activity_home_drawer_layout);


        NavigationView HomeActivityNavView = findViewById(R.id.activity_home_nav_menu);
        HomeActivityNavView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        HomeActivityNavDrawer.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                HomeActivityNavDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
