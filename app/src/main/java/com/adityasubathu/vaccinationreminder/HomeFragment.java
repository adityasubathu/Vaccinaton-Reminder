package com.adityasubathu.vaccinationreminder;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
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
    databaseHandler helper;
    alarmManagerClass alarm;
    ListCreator listCreator = new ListCreator();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = getLayoutInflater().inflate(R.layout.home_fragment, container, false);
        setHasOptionsMenu(true);

        ((MainFragmentHolder) Objects.requireNonNull(getActivity())).setActionBarTitle("Home");
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final childListAdapter adap;

        final ExpandableListView lv = v.findViewById(R.id.childListView);

        helper = new databaseHandler(getActivity());

        childrenList = listCreator.getChildrenList(getActivity());
        DOBList = listCreator.getDOBList(getActivity());

        adap = new childListAdapter(Objects.requireNonNull(getActivity()), childrenList, DOBList, new dataHolder().vaccineList);

        lv.setAdapter(adap);
        lv.setLongClickable(true);

        alarm = new alarmManagerClass();

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String deletedName = (String) adap.getGroup(position);
                Toast.makeText(getActivity(), childrenList.get(position) + " deleted", Toast.LENGTH_SHORT).show();

                helper.delete(deletedName);
                adap.childrenList.remove(position);
                adap.DOBList.remove(position);
                adap.nextDateList.remove(position);
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

            SharedPreferences mySharedPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = mySharedPrefs.edit();
            e.putBoolean("login", false);
            e.apply();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
            Objects.requireNonNull(getActivity()).finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

class childListAdapter extends BaseExpandableListAdapter {

    List<String> childrenList, DOBList, nextDateList = new ArrayList<>();
    private List<String> scheduleVaccinesList;
    private Context context;
    private alarmManagerClass alarm = new alarmManagerClass();

    childListAdapter(Context c, List<String> objects, List<String> objects2, List<String> objects3) {

        context = c;
        childrenList = objects;
        DOBList = objects2;
        scheduleVaccinesList = objects3;
    }

    @Override
    public int getGroupCount() {
        return childrenList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return scheduleVaccinesList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return childrenList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return scheduleVaccinesList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.home_fragment_expandable_listview_adapter_layout, parent, false);

        TextView childNameTextView = convertView.findViewById(R.id.childName);
        TextView dobViewerTextView = convertView.findViewById(R.id.dobViewer);
        TextView offsetViewerTextView = convertView.findViewById(R.id.offsetViewer);
        TextView vaccineListTextView = convertView.findViewById(R.id.nextVaccineList);
        TextView genderViewerTextView = convertView.findViewById(R.id.genderViewer);

        dataHolder dataHolder = new dataHolder();
        String s1 = DOBList.get(groupPosition);
        long nextDateMilliseconds = dataHolder.getNextVaccineDate(s1);

        String vaccineList = dataHolder.getVaccineList(context, groupPosition, ", ");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nextDateMilliseconds);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        ListCreator creator = new ListCreator();

        childNameTextView.setText(childrenList.get(groupPosition));
        dobViewerTextView.setText(DOBList.get(groupPosition));
        genderViewerTextView.setText(creator.getGenderList(context).get(groupPosition));

        nextDateList.add(formatter.format(calendar.getTime()));

        if (nextDateMilliseconds != 1) {
            offsetViewerTextView.setText(nextDateList.get(groupPosition));
            vaccineListTextView.setText(vaccineList);

            boolean alarmNotUp = (PendingIntent.getBroadcast(context, groupPosition, new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmNotUp) {

                Log.d("alarm", "setAlarm called");
                alarm.setAlarm(calendar.getTimeInMillis(), context, groupPosition);


            } else {

                Log.d("alarm", "alarm exists");

            }
        } else {

            offsetViewerTextView.setText("");
            vaccineListTextView.setText(R.string.vacc_complete);

        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = Objects.requireNonNull(inflater).inflate(R.layout.home_fragment_expandable_listview_sublist_layout, parent, false);

        TextView indices, vaccineName, date;

        indices = convertView.findViewById(R.id.indices);
        vaccineName = convertView.findViewById(R.id.vaccinesName);
        date = convertView.findViewById(R.id.VaccineDate);

        indices.setText(String.format(Locale.getDefault(), "%s", Integer.toString(childPosition + 1)));
        vaccineName.setText(scheduleVaccinesList.get(childPosition));

        ListCreator creator = new ListCreator();
        String DOB = DOBList.get(groupPosition);
        List<String> nextVaccinesDateList = creator.getFullVaccineDatesList(DOB);

        date.setText(nextVaccinesDateList.get(childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
