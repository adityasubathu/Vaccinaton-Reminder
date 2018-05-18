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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class AddNewChildFragment extends Fragment {

    View v;
    int selectedGenderId;
    SharedPreferences preferences;
    TextView childNameTextView, DateOfBirth;
    ImageView calenderDialogOpener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.add_new_child_fragment, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RadioGroup childGenderRadioGroup = v.findViewById(R.id.addChildRadioGroup);

        childGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedGenderId = childGenderRadioGroup.getCheckedRadioButtonId();
            }
        });

        childNameTextView = v.findViewById(R.id.fullNameAddChildField);
        String childFullName = childNameTextView.getText().toString();

        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = preferences.edit();

        if (childFullName.isEmpty()) {

            Toast.makeText(getActivity(), R.string.empty_child_name_toast, Toast.LENGTH_SHORT).show();

        }

        if (selectedGenderId == R.id.male) {
            e.putString("childGender", "Male");
        } else {
            e.putString("childGender", "female");
        }
        e.apply();

    }
}
