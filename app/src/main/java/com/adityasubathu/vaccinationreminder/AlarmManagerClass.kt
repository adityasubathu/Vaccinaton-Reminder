package com.adityasubathu.vaccinationreminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

class AlarmManagerClass {
    fun setAlarm(time: Long, context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.timeInMillis = time
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent.putExtra("position", requestCode.toString()), PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
        Log.d("alarm", "alarm set")
    }

    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        Log.d("alarm", "alarmManager has been cancelled")
    }
}