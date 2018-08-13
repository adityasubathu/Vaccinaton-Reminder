package com.adityasubathu.vaccinationreminder;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OffsetCalculator {

    //Array of vaccines, in chronological order
    public String[] vaccineList = {"BCG", "OPV 0", "Hep–B 1", "DTwP 1", "IPV 1", "Hep–B 2", "Hib 1", "Rotavirus 1", "PCV 1", "DTwP 2", "IPV 2", "Hib 2", "Rotavirus 2", "PCV 2", "DTwP 3", "IPV 3", "Hib 3", "Rotavirus 3", "PCV 3", "OPV 1", "Hep–B 3", "OPV 2", "MMR 1", "TCV", "Hep–A 1", "MMR 2", "Varicella 1", "PCV booster", "DTwP B1/DTaP B1", "IPV B1", "Hib B1", "Hep–A 2", "Booster of Typhoid", "Conjugate Vaccine", "DTwP B2/DTaP B2", "OPV 3", "Varicella 2", "MMR 3", "Tdap/Td", "HPV"};

    //Array for number of weeks each of the above vaccine is due
    public int[] weekList = {1, 1, 1, 6, 6, 6, 6, 6, 6, 10, 10, 10, 10, 10, 14, 14, 14, 14, 14, 26, 26, 36, 36, 52, 52, 60, 60, 60, 72, 72, 72, 72, 104, 104, 208, 208, 208, 208, 520, 520};

    private List<String> nextVaccines = new ArrayList<>();

    private int offset;

    public String getVaccineList(Context context, int position, String appendString) {

        ListCreator listCreator = new ListCreator();

        List<String> DOBList = listCreator.getDOBList(context);

        String DOB = DOBList.get(position);

        getNextVaccineDate(DOB);

        for (int i = 0; i < weekList.length; i++) {

            if (weekList[i] == offset) {

                nextVaccines.add(vaccineList[i]);

            }

        }

        StringBuilder vaccineListStringBuilder = new StringBuilder();

        for (int j = 0; j < nextVaccines.size(); j++) {

            if (j == nextVaccines.size() - 1) {

                vaccineListStringBuilder.append(nextVaccines.get(j)).append("");
            } else {

                vaccineListStringBuilder.append(nextVaccines.get(j)).append(appendString);
            }

        }

        return vaccineListStringBuilder.toString();
    }

    long getNextVaccineDate(String dateOfBirth) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar timeNow = Calendar.getInstance();

        timeNow.setTimeInMillis(System.currentTimeMillis());

        Calendar DOBCalendar = Calendar.getInstance();

        Date DOB = null;

        try {
            DOB = sdf.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (DOB != null) {

            DOBCalendar.setTime(DOB);

        }

        long dobInMillis = DOBCalendar.getTimeInMillis();

        long elapsedTimeSinceBirthInMillis = System.currentTimeMillis() - dobInMillis;

        int elapsedTimeSinceBirthInWeeks = (int) (elapsedTimeSinceBirthInMillis / 1000 / 60 / 60 / 24 / 7);

        if (elapsedTimeSinceBirthInWeeks > 520) {

            return 1;

        }

        for (int weeksInterval : weekList) {

            if (weeksInterval > elapsedTimeSinceBirthInWeeks) {

                offset = weeksInterval;
                break;

            }

        }

        Calendar nextVaccineDateCalendar = Calendar.getInstance();
        nextVaccineDateCalendar.setTime(DOB);

        int year = offset / 52;
        int weeks = offset - (52 * year);

        nextVaccineDateCalendar.add(Calendar.YEAR, year);
        nextVaccineDateCalendar.add(Calendar.WEEK_OF_YEAR, weeks);

        return nextVaccineDateCalendar.getTimeInMillis();

    }

}
