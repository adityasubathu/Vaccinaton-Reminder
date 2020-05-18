package com.adityasubathu.vaccinationreminder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
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

class LoginActivitySignUpFragment : Fragment(), View.OnClickListener, OnTouchListener {
    private lateinit var emailSignupField: EditText
    private lateinit var usernameSignupField: EditText
    private lateinit var passwordSignupField: EditText
    private lateinit var passwordConfirmSignupField: EditText
    private lateinit var fullNameSignupField: EditText
    private lateinit var mySharedPrefs: SharedPreferences
    private lateinit var loginFlagPrefs: SharedPreferences
    private lateinit var loginLauncherTextView: TextView
    private lateinit var v: View
    private lateinit var signupButton: Button
    private lateinit var passwordVisible: ImageView
    private lateinit var passwordConfirmVisible: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.sign_up_fragment, container, false)
        return v
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()

        val logFrag = LoginActivityLoginFragment()

        username = logFrag.username
        password = logFrag.password
        emailSignupField = v.findViewById(R.id.emailSignupField)
        usernameSignupField = v.findViewById(R.id.usernameSignupField)
        passwordSignupField = v.findViewById(R.id.passwordSignupField)
        fullNameSignupField = v.findViewById(R.id.fullNameSignupField)
        loginLauncherTextView = v.findViewById(R.id.loginLauncherTextView)
        passwordConfirmSignupField = v.findViewById(R.id.passwordConfirmSignupField)
        passwordVisible = v.findViewById(R.id.password_signup_visibility)
        passwordConfirmVisible = v.findViewById(R.id.password_signup_confirm_visibility)
        loginLauncherTextView.setOnClickListener {
            val loginFragmentObject = LoginActivityLoginFragment()
            val fm = activity!!.supportFragmentManager
            val ft = fm.beginTransaction()
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            ft.replace(R.id.loginActivityFragmentHolder, loginFragmentObject, "LoginActivityLoginFragment")
            ft.commit()
        }
        if (username.isNotEmpty()) {
            usernameSignupField.setText(username)
        }
        if (password != null && password!!.isNotEmpty()) {
            passwordSignupField.setText(password)
        }
        fullNameSignupField.requestFocus()
        signupButton = v.findViewById(R.id.signupButton)
        signupButton.setOnClickListener(this)
        passwordVisible.setOnTouchListener(this)
        passwordConfirmVisible.setOnTouchListener(this)
    }

    override fun onClick(v: View) {
        email = emailSignupField.text.toString()
        fullName = fullNameSignupField.text.toString()
        username = usernameSignupField.text.toString()
        password = passwordSignupField.text.toString()
        val confirmPassword = passwordConfirmSignupField.text.toString()
        mySharedPrefs = activity!!.getSharedPreferences(username, Context.MODE_PRIVATE)
        loginFlagPrefs = activity!!.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        val storedEmail = mySharedPrefs.getString("email", null)
        val storedUsername = mySharedPrefs.getString("username", null)
        if (username.isEmpty() || password!!.isEmpty() || email!!.isEmpty() || fullName!!.isEmpty() || email == storedEmail || username == storedUsername || password != confirmPassword) {
            if (fullName!!.isEmpty()) {
                changeColor(fullNameSignupField, "Please Enter Your Name")
            }
            if (password!!.isEmpty()) {
                changeColor(passwordSignupField, "Please Enter a Password")
            }
            if (email!!.isEmpty()) {
                changeColor(emailSignupField, "Please Enter Your Email")
            }
            if (username.isEmpty()) {
                changeColor(usernameSignupField, "Please Enter a Username")
            }
            if (email == storedEmail) {
                changeColor(emailSignupField, "Email Already Registered")
            }
            if (username == storedUsername) {
                changeColor(usernameSignupField, "Username Already Registered")
            }
            if (password != confirmPassword) {
                changeColor(passwordSignupField, "Passwords Do Not Match")
                changeColor(passwordConfirmSignupField, "Passwords Do Not Match")
            }
        } else {
            val e = mySharedPrefs.edit()
            val e1 = loginFlagPrefs.edit()
            e.putString("username", username)
            e.putString("password", password)
            e.putString("email", email)
            e.putString("fullName", fullName)
            e1.putBoolean("loggedIn", true)
            e1.putString("activeUser", username)
            activeUsername = username
            e.apply()
            e1.apply()
            val i = Intent(activity, MainFragmentHolder::class.java)
            startActivity(i)
            activity!!.finish()
        }
    }

    private fun changeColor(field: EditText, errorMessage: String) {

        //field.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_info_error), null);
        field.background.setColorFilter(resources.getColor(R.color.error_red), PorterDuff.Mode.SRC_ATOP)
        field.setHintTextColor(resources.getColor(R.color.error_red))
        field.error = errorMessage
        field.requestFocus()
        field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                field.setCompoundDrawables(null, null, null, null)
                field.background.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
                field.setHintTextColor(resources.getColor(R.color.textColorBlack))
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val id = view.id
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                if (id == R.id.password_signup_visibility) {
                    passwordSignupField.transformationMethod = null
                    passwordSignupField.setSelection(passwordSignupField.text.length)
                } else if (id == R.id.password_signup_confirm_visibility) {
                    passwordConfirmSignupField.transformationMethod = null
                    passwordConfirmSignupField.setSelection(passwordConfirmSignupField.text.length)
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (id == R.id.password_signup_visibility) {
                    passwordSignupField.transformationMethod = PasswordTransformationMethod.getInstance()
                    passwordSignupField.setSelection(passwordSignupField.text.length)
                } else if (id == R.id.password_signup_confirm_visibility) {
                    passwordConfirmSignupField.transformationMethod = PasswordTransformationMethod.getInstance()
                    passwordConfirmSignupField.setSelection(passwordConfirmSignupField.text.length)
                }
                return false
            }
        }
        return true
    }

    companion object {
        var email: String? = null
        var password: String? = null
        lateinit var username: String
        var fullName: String? = null
        lateinit var activeUsername: String
    }
}