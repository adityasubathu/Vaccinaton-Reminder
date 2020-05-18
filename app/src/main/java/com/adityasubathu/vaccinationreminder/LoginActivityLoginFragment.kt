package com.adityasubathu.vaccinationreminder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class LoginActivityLoginFragment : Fragment(), View.OnClickListener, OnTouchListener {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var signUpLauncherTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var mySharedPrefs: SharedPreferences
    private lateinit var loginFlagPrefs: SharedPreferences
    private lateinit var v: View
    private lateinit var passwordVisible: ImageView

    var password: String? = null
    lateinit var username: String
    private var storedUsername: String? = null
    private var storedPassword: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.login_fragment, container, false)
        return v
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usernameField = v.findViewById(R.id.usernameField)
        passwordField = v.findViewById(R.id.passwordField)
        passwordVisible = v.findViewById(R.id.password_login_visibility)
        passwordVisible.setOnTouchListener(this)
        usernameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                changeColor(usernameField, null, R.color.colorPrimary)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        passwordField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                changeColor(passwordField, null, R.color.colorPrimary)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        loginButton = v.findViewById(R.id.loginButton)
        loginButton.setOnClickListener(this)
        signUpLauncherTextView = v.findViewById(R.id.signUpLauncherTextView)
        signUpLauncherTextView.setOnClickListener {
            username = usernameField.text.toString()
            password = passwordField.text.toString()
            val signUpFragment = LoginActivitySignUpFragment()
            val fm = activity!!.supportFragmentManager
            val ft = fm.beginTransaction()
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            ft.replace(R.id.loginActivityFragmentHolder, signUpFragment, "LoginActivitySignUpFragment")
            ft.commit()
        }
    }

    override fun onStart() {
        super.onStart()
        if (SplashActivity.publicUsername.isNotEmpty()) {
            usernameField.setText(SplashActivity.publicUsername)
            passwordField.requestFocus()
        } else {
            usernameField.requestFocus()
        }
    }

    override fun onClick(v: View) {
        username = usernameField.text.toString()
        password = passwordField.text.toString()
        mySharedPrefs = activity!!.getSharedPreferences(username, Context.MODE_PRIVATE)
        loginFlagPrefs = activity!!.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        storedUsername = mySharedPrefs.getString("username", null)
        storedPassword = mySharedPrefs.getString("password", null)
        if (username.isEmpty() || password!!.isEmpty() || username != storedUsername) {
            if (username.isEmpty()) {
                changeColor(usernameField, "Please Enter Your Username", R.color.error_red)
            }
            if (password!!.isEmpty()) {
                changeColor(passwordField, "Password Cannot Be Empty", R.color.error_red)
            }
            if (username != storedUsername) {
                changeColor(usernameField, "Username Not Registered", R.color.error_red)
            }
        } else if (username == storedUsername && password != storedPassword) {
            changeColor(passwordField, "Password is Incorrect", R.color.error_red)
        } else if (username == storedUsername && password == storedPassword) {
            val e = loginFlagPrefs.edit()
            e.putString("activeUser", username)
            LoginActivitySignUpFragment.activeUsername = username
            e.putBoolean("loggedIn", true)
            e.apply()
            val i = Intent(activity, MainFragmentHolder::class.java)
            startActivity(i)
            activity!!.finish()
        }
    }

    private fun changeColor(field: EditText, errorMessage: String?, color: Int) {
        field.background.setTint(color)
        field.setHintTextColor(color)
        if (errorMessage!=null) {
            field.error = errorMessage
        }
        field.requestFocus()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                passwordField.transformationMethod = null
                passwordField.setSelection(passwordField.text.length)
                return true
            }
            MotionEvent.ACTION_UP -> {
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordField.setSelection(passwordField.text.length)
                return false
            }
        }
        return true
    }
}