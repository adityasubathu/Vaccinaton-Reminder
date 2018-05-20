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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    View v;
    List<String> childrenList;
    String[] arr;
    databaseHandler helper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = getLayoutInflater().inflate(R.layout.home_fragment, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        childListAdapter adap;

        ListView lv = v.findViewById(R.id.childListView);
        helper = new databaseHandler(getActivity());
        childrenList = new ArrayList<>();

        String data = helper.getData();

        arr = data.split(" ");

        if (arr[0].equals("1")) {
            childrenList.add(arr[1]);
        }

        adap = new childListAdapter(Objects.requireNonNull(getActivity()), childrenList);

        lv.setAdapter(adap);

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
    private List<String> list;

    childListAdapter(Context context, List<String> objects) {

        c = context;
        list = objects;

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

        String s = list.get(position);

        tv.setText(list.get(position));

        return convertView;
    }
}


