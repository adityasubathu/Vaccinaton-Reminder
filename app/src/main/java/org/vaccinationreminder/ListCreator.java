package org.vaccinationreminder;

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

    public List<String> getFullVaccineList() {

        OffsetCalculator calculator = new OffsetCalculator();

        List<String> fullVaccineList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();

        for (int i = 1; i < calculator.weekList.length; i++) {

            if (calculator.weekList[i - 1] == calculator.weekList[i]) {

                tempList.add(calculator.vaccineList[i - 1]);

            } else {

                tempList.add(calculator.vaccineList[i]);
                StringBuilder s = new StringBuilder();

                for (int j = 0; j < tempList.size(); j++) {

                    if (j == tempList.size() - 1) {

                        s.append(tempList.get(j)).append("");
                    } else {

                        if (j == 2) {

                            s.append(tempList.get(j)).append("\n");

                        }

                        s.append(tempList.get(j)).append(", ");
                    }
                }

                String appendedVaccineList = s.toString();
                fullVaccineList.add(appendedVaccineList);
                tempList.clear();

            }

        }
        fullVaccineList.add("Tdap/Td, HPV");

        return fullVaccineList;

    }

    public List<String> getFullVaccineDatesList(String dateOfBirth) {

        OffsetCalculator calculator = new OffsetCalculator();

        List<String> fullVaccineDatesList = new ArrayList<>();
        List<Integer> temporaryList = new ArrayList<>();

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


        for (int i = 1; i < calculator.weekList.length; i++) {


            if (calculator.weekList[i - 1] != calculator.weekList[i]) {

                temporaryList.add(calculator.weekList[i]);

            }

        }

        for (int i = 0; i < temporaryList.size(); i++) {

            int year = temporaryList.get(i) / 52;
            int weeks = temporaryList.get(i) - (52 * year);

            calendar.add(Calendar.YEAR, year);
            calendar.add(Calendar.WEEK_OF_YEAR, weeks);

            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {

                fullVaccineDatesList.add(formatter.format(calendar.getTime()));
            }

            calendar.setTime(DOB);

        }

        calendar.setTime(DOB);
        calendar.add(Calendar.YEAR, 10);
        fullVaccineDatesList.add(formatter.format(calendar.getTime()));

        return fullVaccineDatesList;
    }
}
