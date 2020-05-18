package com.adityasubathu.vaccinationreminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    // todo: check why this isn't working
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "An Alarm Has Been Triggered", Toast.LENGTH_SHORT).show()
        val s = intent.getStringExtra("position")
        val i = Intent(context, AlarmAlertWindow::class.java)
        i.putExtra("position", s)
        context.startActivity(i)
    }
}