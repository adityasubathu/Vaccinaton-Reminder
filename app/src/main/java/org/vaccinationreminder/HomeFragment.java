package org.vaccinationreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

public class HomeFragment extends Fragment {

    View v;
    TextView welcomeNameField;
    String[] arr;
    String firstName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = getLayoutInflater().inflate(R.layout.home_fragment, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        welcomeNameField = v.findViewById(R.id.nameTextView);

        SharedPreferences myPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String name = myPrefs.getString("fullName", null);

        if (name != null) {

            arr = name.split(" ");

        }

        if (arr != null) {

            firstName = arr[0];
            welcomeNameField.setText(firstName);

        }
    }
}
