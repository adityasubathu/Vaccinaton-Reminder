package org.vaccinationreminder;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
    List<String> DOBList;
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
        long nextDateMilliseconds = offsetCalculator.dateDiffNextDate(DOBList.get(position));

        String vaccineList = offsetCalculator.getVaccineList(c, position, "\n");

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(nextDateMilliseconds);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        childNameTextView.setText(childrenList.get(position));
        dobViewerTextView.setText(DOBList.get(position));


        if (nextDateMilliseconds != 1) {

            offsetViewerTextView.setText(formatter.format(calendar.getTime()));
            vaccineListTextView.setText(vaccineList);


            boolean alarmUp = (PendingIntent.getBroadcast(c, position, new Intent(c, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUp) {

                alarm.setAlarm(calendar.getTimeInMillis(), c, position);
                Log.d("alarm", "setAlarm called");
            } else {

                Log.d("alarm", "alarm exists");

            }
        } else {

            offsetViewerTextView.setText("");
            vaccineListTextView.setText(R.string.vacc_complete);

        }

        return convertView;
    }
}
