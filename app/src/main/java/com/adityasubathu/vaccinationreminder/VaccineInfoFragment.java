package com.adityasubathu.vaccinationreminder;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VaccineInfoFragment extends Fragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.vaccine_info_fragment, container, false);
        ((MainFragmentHolder) Objects.requireNonNull(getActivity())).setActionBarTitle("Vaccines Info");
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView lv = v.findViewById(R.id.vaccineListView);
        List<String> weeks = new ArrayList<>();

        dataHolder dataHolder = new dataHolder();

        for (int i = 1; i < dataHolder.weekList.size(); i++) {

            weeks.add(Integer.toString(dataHolder.weekList.get(i)));
        }

        vaccineListAdapter adap = new vaccineListAdapter(getActivity(), weeks);
        lv.setAdapter(adap);

    }
}

class vaccineListAdapter extends BaseAdapter {

    private Context c;
    private List<String> weeks;

    vaccineListAdapter(Context context, List<String> arr) {

        c = context;
        weeks = arr;

    }


    @Override
    public int getCount() {
        return weeks.size();
    }

    @Override
    public Object getItem(int position) {
        return weeks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = Objects.requireNonNull(inflater).inflate(R.layout.vaccine_list_adapter, parent, false);

        dataHolder dataHolder = new dataHolder();

        TextView vaccineNameTextView = convertView.findViewById(R.id.VaccineName);
        TextView weeksNumberTextView = convertView.findViewById(R.id.WeekNumber);
        TextView indexTextView = convertView.findViewById(R.id.index);

        vaccineNameTextView.setText(dataHolder.vaccineList.get(position));

        if (weeks.get(position).equals("1")) {

            weeksNumberTextView.setText("Birth");

        } else {

            weeksNumberTextView.setText(weeks.get(position) + " Weeks");
        }

        indexTextView.setText(Integer.toString(position + 1));

        return convertView;
    }
}