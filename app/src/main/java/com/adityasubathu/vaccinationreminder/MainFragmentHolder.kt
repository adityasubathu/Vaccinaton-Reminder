package com.adityasubathu.vaccinationreminder

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainFragmentHolder : AppCompatActivity() {

    private lateinit var homeActivityNavDrawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_holder)

        val toolbar = findViewById<Toolbar>(R.id.activity_home_toolbar)
        setSupportActionBar(toolbar)

        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val homeFragment = HomeFragment()
        val childManagerFragment = AddNewChildFragment()
        val vaccineInfoFragment = VaccineInfoFragment()
        val alarmManagerFragment = AlarmManagerFragment()

        fragmentTransaction.add(R.id.fragment_holder, homeFragment, "home")
        fragmentTransaction.commit()

        homeActivityNavDrawer = findViewById(R.id.activity_home_drawer_layout)
        val homeActivityNavView = findViewById<NavigationView>(R.id.activity_home_nav_menu)
        homeActivityNavView.setCheckedItem(R.id.home_fragment)
        homeActivityNavView.setNavigationItemSelectedListener { menuItem ->
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            menuItem.isChecked = true
            homeActivityNavDrawer.closeDrawers()
            if (menuItem.itemId == R.id.home_fragment) {
                ft.replace(R.id.fragment_holder, homeFragment, "home")
            }
            if (menuItem.itemId == R.id.child_manager) {
                ft.replace(R.id.fragment_holder, childManagerFragment, "childManager")
            }
            if (menuItem.itemId == R.id.vaccines_info) {
                ft.replace(R.id.fragment_holder, vaccineInfoFragment, "vaccineInfo")
            }
            if (menuItem.itemId == R.id.alarm_manager) {
                ft.replace(R.id.fragment_holder, alarmManagerFragment, "alarmManager")
            }
            ft.commit()
            true
        }
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            homeActivityNavDrawer.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}