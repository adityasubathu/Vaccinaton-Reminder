package com.adityasubathu.vaccinationreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override

    // todo: check why this isn't working
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "An Alarm Has Been Triggered", Toast.LENGTH_SHORT).show();

        String s = intent.getStringExtra("position");

        Intent i = new Intent(context, AlarmAlertWindow.class);
        i.putExtra("position", s);

        context.startActivity(i);

    }
}