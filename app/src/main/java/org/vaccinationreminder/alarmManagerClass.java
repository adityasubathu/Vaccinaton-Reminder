package org.vaccinationreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class alarmManagerClass {

    public static List<String> alarmTitleList = new ArrayList<>();


    public void setAlarm(long time, String title, Context context, int requestCode) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.clear();

        calendar.setTimeInMillis(time);

        // TODO: 24/6/18 recreate alarmTitleList on each startup

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.e("alarm", "alarm set");
        } else {

            Log.e("alarm", "alarmManager.set is null");

        }

    }

    public void cancelAlarm(Context context, int requestCode) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {

            alarmManager.cancel(pendingIntent);
            Toast.makeText(context, "An Alarm Has Been Cancelled", android.widget.Toast.LENGTH_SHORT).show();
        } else {

            Log.e("alarm", "alarmManager.cancel is null");

        }


    }

}
