package org.vaccinationreminder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    View v;
    List<String> childrenList, DOBList;
    String[] arr;
    databaseHandler helper;
    alarmManagerClass alarm;

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
        childrenList = new ArrayList<>();
        DOBList = new ArrayList<>();

        String data = helper.getData();

        arr = data.split(" {2}");

        for (int i = 0; i < arr.length; i++) {

            if (isInteger(arr[i])) {
                childrenList.add(arr[i + 1]);
                DOBList.add(arr[i + 2]);
            }

        }

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
                adap.notifyDataSetChanged();
                alarm.cancelAlarm(getActivity(), position);
                return true;
            }
        });

    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
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
    private List<String> list, list2;

    private alarmManagerClass alarm = new alarmManagerClass();
    childListAdapter(Context context, List<String> objects, List<String> objects2) {

        c = context;
        list = objects;
        list2 = objects2;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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

        long offsetMilliSeconds = offsetCalculator.getNextDate(list2.get(position));

        StringBuilder vaccineList = new StringBuilder();

        for (int j = 0; j < offsetCalculator.nextVaccines.size(); j++) {

            if (j == offsetCalculator.nextVaccines.size() - 1) {

                vaccineList.append(offsetCalculator.nextVaccines.get(j)).append("");
            } else {

                vaccineList.append(offsetCalculator.nextVaccines.get(j)).append("\n");
            }

        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(offsetMilliSeconds);

        //setSilentReminder(offsetMilliSeconds, "Get Vaccinations " + vaccineList);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        childNameTextView.setText(list.get(position));
        dobViewerTextView.setText(list2.get(position));
        offsetViewerTextView.setText(formatter.format(calendar.getTime()));
        vaccineListTextView.setText(vaccineList);

        alarm.setAlarm(calendar.getTimeInMillis(), "Get Vaccinations " + vaccineList, c, position+1);

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


