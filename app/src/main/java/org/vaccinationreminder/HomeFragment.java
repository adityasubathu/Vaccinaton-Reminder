package org.vaccinationreminder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    View v;
    List<String> childrenList;
    static List<String> DOBList;
    databaseHandler helper;
    alarmManagerClass alarm;
    ListCreator listCreator = new ListCreator();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = getLayoutInflater().inflate(R.layout.home_fragment, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final childListAdapter adap;

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR}, 1);
            }

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALENDAR}, 1);
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR}, 1);

        }

        final ListView lv = v.findViewById(R.id.childListView);

        helper = new databaseHandler(getActivity());

        childrenList = listCreator.getChildrenList(getActivity());
        DOBList = listCreator.getDOBList(getActivity());

        adap = new childListAdapter(Objects.requireNonNull(getActivity()), childrenList, DOBList);

        lv.setAdapter(adap);
        lv.setLongClickable(true);

        alarm = new alarmManagerClass();

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String deletedName = (String) adap.getItem(position);

                Toast.makeText(getActivity(), childrenList.get(position) + " deleted", Toast.LENGTH_SHORT).show();

                helper.delete(deletedName);
                adap.childrenList.remove(position);
                adap.notifyDataSetChanged();
                alarm.cancelAlarm(getActivity(), position);
                return true;
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.home_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout) {

            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
            Objects.requireNonNull(getActivity()).finish();
        }

        return super.onOptionsItemSelected(item);
    }
}


class childListAdapter extends BaseAdapter {

    private Context c;
    List<String> childrenList;
    private List<String> DOBList;
    private ListCreator listCreator = new ListCreator();

    private alarmManagerClass alarm = new alarmManagerClass();

    childListAdapter(Context context, List<String> objects, List<String> objects2) {

        c = context;
        childrenList = objects;
        DOBList = objects2;

    }

    @Override
    public int getCount() {
        return childrenList.size();
    }

    @Override
    public Object getItem(int position) {
        return childrenList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.home_fragment_listview_adapter, parent, false);

        TextView childNameTextView = convertView.findViewById(R.id.childName);
        TextView dobViewerTextView = convertView.findViewById(R.id.dobViewer);
        TextView offsetViewerTextView = convertView.findViewById(R.id.offsetViewer);
        TextView vaccineListTextView = convertView.findViewById(R.id.nextVaccineList);

        OffsetCalculator offsetCalculator = new OffsetCalculator();
        long offsetMilliSeconds = offsetCalculator.getNextDate(DOBList.get(position));

        String vaccineList = listCreator.getVaccineList(c, position);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(offsetMilliSeconds);

        //setSilentReminder(offsetMilliSeconds, "Get Vaccinations " + vaccineList);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        childNameTextView.setText(childrenList.get(position));
        dobViewerTextView.setText(DOBList.get(position));


        if (offsetMilliSeconds == 0) {

            vaccineListTextView.setText("Vaccination Complete");
            offsetViewerTextView.setText("No Next Vaccination Date");

        } else {

            offsetViewerTextView.setText(formatter.format(calendar.getTime()));
            vaccineListTextView.setText(vaccineList);
        }

        boolean alarmUp = (PendingIntent.getBroadcast(c, position, new Intent(c, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {

            alarm.setAlarm(calendar.getTimeInMillis(), "Get Vaccinations:\n" + vaccineList, c, position);
            Log.e("alarm", "setAlarm called");
        } else {

            Log.e("alarm", "alarm exists");

        }

        return convertView;
    }

//    private void setSilentReminder(long startTime, String title) {
//
//        ContentResolver cr = c.getContentResolver();
//        ContentValues values = new ContentValues();
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(startTime);
//
//        String dtStart = formatter.format(calendar.getTime());
//
//        values.put(CalendarContract.Events.DTSTART, dtStart);
//        calendar.add(Calendar.DATE, 1);
//        String dtEnd = formatter.format(calendar.getTime());
//        values.put(CalendarContract.Events.DTEND, dtEnd);
//        values.put(CalendarContract.Events.TITLE, title);
//        values.put(CalendarContract.Events.DESCRIPTION, "");
//
//        TimeZone timeZone = TimeZone.getDefault();
//        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
//
//// Default calendar
//        values.put(CalendarContract.Events.CALENDAR_ID, 1);
//// Set Period for 1 Hour
//        values.put(CalendarContract.Events.ALL_DAY, true);
//
//        values.put(CalendarContract.Events.HAS_ALARM, true);
//
//        values.put(CalendarContract.Events.EVENT_TIMEZONE,TimeZone.getDefault().getID());
//        Log.i("Timezone", "Timezone retrieved=>"+TimeZone.getDefault().getID());
//
//// Insert event to calendar
//        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.checkSelfPermission(c, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions((Activity) c, new String[]{Manifest.permission.READ_CALENDAR,
//                        Manifest.permission.WRITE_CALENDAR}, 1);
//            }
//
//            ActivityCompat.requestPermissions((Activity) c, new String[]{Manifest.permission.READ_CALENDAR}, 1);
//        }
//
//        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions((Activity) c, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
//
//        }
//
//
//        Cursor cursor =
//                CalendarContract.Instances.query(c.getContentResolver(), null, startTime, startTime + (24 * 60 * 60 * 1000), title);
//        if (cursor.getCount() > 0) {
//            Toast.makeText(c, "Event already exists", Toast.LENGTH_SHORT).show();
//        } else {
//
//            try {
//
//                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
//                Log.i("insertion", "Uri returned=>"+ Objects.requireNonNull(uri).toString());
//                // get the event ID that is the last element in the Uri
//                long eventID = Long.parseLong(uri.getLastPathSegment());
//
//                ContentValues reminders = new ContentValues();
//                reminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
//                reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
//                reminders.put(CalendarContract.Reminders.MINUTES, 10);
//
//                Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);
//
//            } catch (Exception e) {
//
//                e.printStackTrace();
//
//            }
//        }
//
//    }
}


