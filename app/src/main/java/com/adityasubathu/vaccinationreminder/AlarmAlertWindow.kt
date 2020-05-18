package com.adityasubathu.vaccinationreminder

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AlarmAlertWindow : AppCompatActivity() {
    private var alarmAlertVaccineList: TextView? = null
    private var alarmAlertChildName: TextView? = null
    private var alarmAlertCurrentSysTime: TextView? = null
    private var alarmAlertDismissButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_alert_window)
        alarmAlertChildName = findViewById(R.id.alarmAlertChildName)
        alarmAlertVaccineList = findViewById(R.id.alarmAlertVaccineList)
        alarmAlertCurrentSysTime = findViewById(R.id.alarmAlertCurrentDateTime)
        alarmAlertDismissButton = findViewById(R.id.alarmAlertDismissButton)
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        alarmAlertCurrentSysTime?.text = formatter.format(calendar.time)

        //alarmAlertVaccineList.setText();
        alarmAlertDismissButton?.setOnClickListener { finish() }
    }
}