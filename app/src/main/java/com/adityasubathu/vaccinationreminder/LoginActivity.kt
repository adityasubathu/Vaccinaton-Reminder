package com.adityasubathu.vaccinationreminder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginFragment = LoginActivityLoginFragment()
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(R.id.loginActivityFragmentHolder, loginFragment, "LoginActivityLoginFragment")
        ft.commit()
    }
}