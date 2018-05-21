package org.vaccinationreminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
        childrenList = new ArrayList<>();
        DOBList = new ArrayList<>();

        String data = helper.getData();

        arr = data.split(" {2}");

        for (int i=0; i < arr.length; i++) {

            if (isInteger(arr[i])) {
                childrenList.add(arr[i+1]);
                DOBList.add(arr[i+2]);
            }

        }

        adap = new childListAdapter(Objects.requireNonNull(getActivity()), childrenList, DOBList);

        lv.setAdapter(adap);
        lv.setLongClickable(true);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String deletedName = (String) adap.getItem(position);

                helper.delete(deletedName);
                adap.notifyDataSetChanged();

                return false;
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

        TextView tv = convertView.findViewById(R.id.childName);
        TextView tv2 = convertView.findViewById(R.id.dobViewer);
        TextView tv3 = convertView.findViewById(R.id.offsetViewer);
        TextView tv4 = convertView.findViewById(R.id.nextVaccineList);

        Function function = new Function();

        long offsetMilliSeconds = function.getNextDate(list2.get(position));

        StringBuilder vaccineList = new StringBuilder();

        for (String s : function.nextVaccines)
        {
            vaccineList.append(s).append("\n");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(offsetMilliSeconds);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        tv.setText(list.get(position));
        tv2.setText(list2.get(position));
        tv3.setText(formatter.format(calendar.getTime()));
        tv4.setText(vaccineList);

        return convertView;
    }
}


