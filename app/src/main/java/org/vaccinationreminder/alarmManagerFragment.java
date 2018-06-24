package org.vaccinationreminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class alarmManagerFragment extends Fragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.alarm_manager_fragment, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView lv = v.findViewById(R.id.alarmManagerFragmentListView);
        List<String> alarmTitleList = new ArrayList<>();
        ListCreator listCreator = new ListCreator();

        for (int i = 0; i < listCreator.getChildrenList(getActivity()).size(); i++){

            alarmTitleList.add(listCreator.getVaccineList(getActivity(), i));

        }

        alarmManagerFragmentAdapter adapter = new alarmManagerFragmentAdapter(getActivity(), alarmTitleList);

        lv.setAdapter(adapter);

    }
}


class alarmManagerFragmentAdapter extends BaseAdapter {

    private Context context;
    private ListCreator listCreator = new ListCreator();
    private List<String> alarmTitleList;

    alarmManagerFragmentAdapter(Context c, List<String> titleList) {
        context = c;
        alarmTitleList = titleList;
    }


    @Override
    public int getCount() {
        return alarmTitleList.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmTitleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OffsetCalculator offsetCalculator = new OffsetCalculator();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = Objects.requireNonNull(inflater).inflate(R.layout.alarm_manager_fragment_listview_adapter, null);

        TextView alarmTitleTextView = convertView.findViewById(R.id.alarmTitle);
        TextView alarmDateTextView = convertView.findViewById(R.id.alarmDate);
        TextView alarmTriggerTimeTextView = convertView.findViewById(R.id.triggerTimeDisplay);
        TextView alarmRemainingTimeTextView = convertView.findViewById(R.id.remainingTime);
        TextView nameOfChildTextView = convertView.findViewById(R.id.alarmChildName);

        long offsetMilliSeconds = offsetCalculator.getNextDate(listCreator.getDOBList(context).get(position));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(offsetMilliSeconds);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE MMMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        String remainingTime = getRemainingTime(position);
        String date = dateFormatter.format(calendar.getTime());

        alarmDateTextView.setText(date);
        alarmTriggerTimeTextView.setText(timeFormatter.format(calendar.getTime()));
        alarmTitleTextView.setText(alarmTitleList.get(position));
        alarmRemainingTimeTextView.setText(remainingTime);
        nameOfChildTextView.setText(listCreator.getChildrenList(context).get(position));

        return convertView;
    }

    private String getRemainingTime(int position){

        OffsetCalculator offsetCalculator = new OffsetCalculator();

        long offsetMilliSeconds = offsetCalculator.getNextDate(listCreator.getDOBList(context).get(position));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(offsetMilliSeconds);

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(System.currentTimeMillis());

        long millis = calendar.getTimeInMillis() - currentTime.getTimeInMillis();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = millis / daysInMilli;
        millis = millis % daysInMilli;

        long elapsedHours = millis / hoursInMilli;
        millis = millis % hoursInMilli;

        long elapsedMinutes = millis / minutesInMilli;
        millis = millis % minutesInMilli;

        long elapsedSeconds = millis / secondsInMilli;

        return String.format(Locale.getDefault(), "%s Days:%s Hours:%s Minutes", Long.toString(elapsedDays), Long.toString(elapsedHours),
                Long.toString(elapsedMinutes));

    }

}