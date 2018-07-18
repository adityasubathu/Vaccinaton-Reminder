package org.vaccinationreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmAlertWindow extends AppCompatActivity {

    TextView alarmAlertVaccineList, alarmAlertChildName, alarmAlertCurrentSysTime;
    Button alarmAlertDismissButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_alert_window);

        alarmAlertChildName = findViewById(R.id.alarmAlertChildName);
        alarmAlertVaccineList = findViewById(R.id.alarmAlertVaccineList);
        alarmAlertCurrentSysTime = findViewById(R.id.alarmAlertCurrentDateTime);
        alarmAlertDismissButton = findViewById(R.id.alarmAlertDismissButton);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmAlertCurrentSysTime.setText(formatter.format(calendar.getTime()));

        alarmAlertDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
