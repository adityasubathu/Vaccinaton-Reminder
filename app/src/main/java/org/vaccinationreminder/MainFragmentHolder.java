package org.vaccinationreminder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Objects;

public class MainFragmentHolder extends AppCompatActivity {

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final HomeFragment homeFragment = new HomeFragment();
        final AddNewChildFragment childManagerFragment = new AddNewChildFragment();

        fragmentTransaction.add(R.id.fragment_holder, homeFragment, "home");
        fragmentTransaction.commit();

        HomeActivityNavDrawer = findViewById(R.id.activity_home_drawer_layout);
        NavigationView HomeActivityNavView = findViewById(R.id.activity_home_nav_menu);
        HomeActivityNavView.setCheckedItem(R.id.home_fragment);

        HomeActivityNavView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        menuItem.setChecked(true);
                        HomeActivityNavDrawer.closeDrawers();

                        if (menuItem.getItemId() == R.id.home_fragment) {
                            fragmentTransaction.replace(R.id.fragment_holder, homeFragment, "home");
                        }

                        if (menuItem.getItemId() == R.id.child_manager) {
                            fragmentTransaction.replace(R.id.fragment_holder, childManagerFragment, "childManager");
                        }
                        fragmentTransaction.commit();
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
