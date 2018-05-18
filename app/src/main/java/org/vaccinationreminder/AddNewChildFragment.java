package org.vaccinationreminder;

import android.app.DatePickerDialog;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

public class AddNewChildFragment extends Fragment {

    View v;
    int selectedGenderId = 0, year, month, day, hour, minute;
    SharedPreferences preferences;
    TextView childNameTextView, DateOfBirthTextView;
    ImageView calenderDialogOpener;
    Button addChildConfirmButton;

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

        addChildConfirmButton = v.findViewById(R.id.addChildConfirmButton);
        childNameTextView = v.findViewById(R.id.fullNameAddChildField);
        calenderDialogOpener = v.findViewById(R.id.childDOBPicker);
        DateOfBirthTextView = v.findViewById(R.id.dateOFBirthAddChildField);

        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor e = preferences.edit();


        calenderDialogOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);

                DatePickerDialog dp = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {

                        DateOfBirthTextView.setText(String.format("%s/%s/%s", d,m,y));

                    }
                }, year, month, day);

                dp.show();
            }
        });

        addChildConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String childFullName = childNameTextView.getText().toString();
                String dateOfBirth = DateOfBirthTextView.getText().toString();

                if (childFullName.isEmpty() || dateOfBirth.isEmpty() || selectedGenderId == 0) {

                    Toast.makeText(getActivity(), R.string.empty_child_name_toast, Toast.LENGTH_SHORT).show();

                } else {

                    e.putString("childName", childFullName);
                    e.putString("childDOB", dateOfBirth);

                    if (selectedGenderId == R.id.male) {
                        e.putString("childGender", "Male");
                    } else {
                        e.putString("childGender", "female");
                    }
                    e.apply();

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    ft.replace(R.id.fragment_holder, new HomeFragment());
                    ft.commit();

                }

            }
        });

    }
}
