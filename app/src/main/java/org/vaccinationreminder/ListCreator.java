package org.vaccinationreminder;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListCreator {

    public List<String> getDOBList (Context context) {

        List<String> DOBList = new ArrayList<>();
        databaseHandler helper = new databaseHandler(context);

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
        databaseHandler helper = new databaseHandler(context);

        String data = helper.getData();

        String[] arr = data.split(" {2}");

        for (int i = 0; i < arr.length; i++) {

            if (isInteger(arr[i])) {
                childrenList.add(arr[i + 1]);
            }
        }

        return childrenList;
    }

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

}
