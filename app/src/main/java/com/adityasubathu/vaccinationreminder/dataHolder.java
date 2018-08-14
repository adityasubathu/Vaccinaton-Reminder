package com.adityasubathu.vaccinationreminder;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class dataHolder {

    private static Date DOB;
    //Array of vaccines, in chronological order
    public List<String> vaccineList = Arrays.asList("BCG, OPV 0, Hep–B 1", "DTwP 1, IPV 1, Hep–B 2, Hib 1, Rotavirus 1, PCV 1", "DTwP 2, IPV 2, Hib 2, Rotavirus 2, PCV 2", "DTwP 3, IPV 3, Hib 3, Rotavirus 3, PCV 3", "OPV 1, Hep–B 3", "OPV 2, MMR 1", "TCV, Hep–A 1", "MMR 2, Varicella 1, PCV booster", "DTwP B1/DTaP B1, IPV B1, Hib B1, Hep–A 2", "Booster of Typhoid, Conjugate Vaccine", "DTwP B2/DTaP B2, OPV 3, Varicella 2, MMR 3", "Tdap/Td, HPV");
    //Array for number of weeks each of the above vaccine is due
    public List<Integer> weekList = Arrays.asList(1, 6, 10, 14, 26, 36, 52, 60, 72, 104, 208, 520);
    private String nextVaccines;

    String getVaccineList(Context context, int position, String appendString) {

        int offset;
        ListCreator listCreator = new ListCreator();
        List<String> DOBList = listCreator.getDOBList(context);

        String dateOfBirth = DOBList.get(position);
        offset = getOffset(dateOfBirth);

        for (int i = 0; i < weekList.size(); i++) {
            if (weekList.get(i) == offset) {
                nextVaccines = vaccineList.get(i);
            }
        }

        if (appendString.equals(", ")) {
            return nextVaccines;
        } else {
            StringBuilder vaccineListStringBuilder = new StringBuilder();

            String[] brokenVaccineList = nextVaccines.split(", ");

            for (int j = 0; j < brokenVaccineList.length; j++) {

                if (j == brokenVaccineList.length - 1) {
                    vaccineListStringBuilder.append(brokenVaccineList[j]).append("");
                } else {
                    vaccineListStringBuilder.append(brokenVaccineList[j]).append(appendString);
                }
            }
            return vaccineListStringBuilder.toString();
        }
    }

    long getNextVaccineDate(String dateOfBirth) {

        int offset;
        offset = getOffset(dateOfBirth);

        if (offset > 520) {
            return 1;
        }

        Calendar nextVaccineDateCalendar = Calendar.getInstance();
        nextVaccineDateCalendar.setTime(DOB);

        int year = offset / 52;
        int weeks = offset - (52 * year);

        nextVaccineDateCalendar.add(Calendar.YEAR, year);
        nextVaccineDateCalendar.add(Calendar.WEEK_OF_YEAR, weeks);

        return nextVaccineDateCalendar.getTimeInMillis();

    }

    private int getOffset(String dateOfBirth) {

        int weeks = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar timeNow = Calendar.getInstance();
        timeNow.setTimeInMillis(System.currentTimeMillis());

        Calendar DOBCalendar = Calendar.getInstance();

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

        for (int weeksInterval : weekList) {
            if (weeksInterval > elapsedTimeSinceBirthInWeeks) {
                weeks = weeksInterval;
                break;
            }
        }
        if (elapsedTimeSinceBirthInWeeks > 520) {
            return 521;
        }
        return weeks;
    }
}
