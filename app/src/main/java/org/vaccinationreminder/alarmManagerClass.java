package org.vaccinationreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class alarmManagerClass {


    public void setAlarm(long time, String title, Context context, int requestCode) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.clear();

        calendar.setTimeInMillis(time);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        boolean alarmUp = (PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmManager != null && alarmUp) {

            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            Toast.makeText(context, "An Alarm Has Been Set", android.widget.Toast.LENGTH_SHORT).show();
            Log.e("alarm", "alarm set");

        } else if (!alarmUp) {

            Log.e("alarm", "alarm already exists");
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
