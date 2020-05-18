package com.adityasubathu.vaccinationreminder

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var loginFlagPrefs: SharedPreferences
    lateinit var activeUser: String
    var loggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        loginFlagPrefs = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        activeUser = loginFlagPrefs.getString("activeUser", null).toString()
        loggedIn = loginFlagPrefs.getBoolean("loggedIn", false)

        object : Thread() {
            override fun run() {
                try {
                    sleep(2500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (!loggedIn) {
                    val i = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                    if (activeUser.isNotEmpty()) {
                        publicUsername = activeUser
                    }
                } else {
                    LoginActivitySignUpFragment.activeUsername = activeUser
                    val i2 = Intent(this@SplashActivity, MainFragmentHolder::class.java)
                    startActivity(i2)
                    finish()
                }
            }
        }.start()
    }

    companion object {
        lateinit var publicUsername: String
    }
}