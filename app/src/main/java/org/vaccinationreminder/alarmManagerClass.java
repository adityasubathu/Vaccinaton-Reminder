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

    public void setAlarm(long time, Context context, int requestCode) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.clear();

        calendar.setTimeInMillis(time);
        //calendar.add(Calendar.HOUR, 8);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Log.d("alarm", "alarm set");

        } else {
            Log.d("alarm", "alarmManager.set is null");
        }

    }

    public void cancelAlarm(Context context, int requestCode) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {

            alarmManager.cancel(pendingIntent);

            Log.d("alarm", "alarmManager has been cancelled");

        } else {
            Log.d("alarm", "alarmManager.cancel is null");
        }


    }

}
