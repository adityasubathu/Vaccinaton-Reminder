package com.adityasubathu.vaccinationreminder;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListCreator {

    private static boolean isInteger(String str) {
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

    public List<String> getDOBList(Context context) {

        List<String> DOBList = new ArrayList<>();
        databaseHandler helper = new databaseHandler(context, LoginActivitySignUpFragment.activeUsername);

        String data = helper.getData();

        String[] arr = data.split(" {2}");

        for (int i = 0; i < arr.length; i++) {

            if (isInteger(arr[i])) {
                DOBList.add(arr[i + 2]);
            }
        }

        return DOBList;
    }

    public List<String> getChildrenList(Context context) {

        List<String> childrenList = new ArrayList<>();
        databaseHandler helper = new databaseHandler(context, LoginActivitySignUpFragment.activeUsername);

        String data = helper.getData();

        String[] arr = data.split(" {2}");

        for (int i = 0; i < arr.length; i++) {

            if (isInteger(arr[i])) {
                childrenList.add(arr[i + 1]);
            }
        }

        return childrenList;
    }

    public List<String> getGenderList(Context context) {

        List<String> genderList = new ArrayList<>();
        databaseHandler helper = new databaseHandler(context, LoginActivitySignUpFragment.activeUsername);

        String data = helper.getData();

        String[] arr = data.split(" {2}");

        for (int i = 0; i < arr.length; i++) {

            if (isInteger(arr[i])) {
                genderList.add(arr[i + 3]);
            }
        }

        return genderList;


    }

    public List<String> getFullVaccineDatesList(String dateOfBirth) {

        dataHolder holder = new dataHolder();

        List<String> fullVaccineDatesList = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date DOB = null;

        try {
            DOB = formatter.parse(dateOfBirth);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();

        if (DOB != null) {

            calendar.setTime(DOB);
        }

        calendar.setTime(DOB);

        for (int i = 0; i < holder.weekList.size(); i++) {

            int year = holder.weekList.get(i) / 52;
            int weeks = holder.weekList.get(i) - (52 * year);

            calendar.add(Calendar.YEAR, year);
            calendar.add(Calendar.WEEK_OF_YEAR, weeks);
            fullVaccineDatesList.add(formatter.format(calendar.getTime()));
            calendar.setTime(DOB);

        }

        calendar.setTime(DOB);
        calendar.add(Calendar.YEAR, 10);

        return fullVaccineDatesList;
    }
}
