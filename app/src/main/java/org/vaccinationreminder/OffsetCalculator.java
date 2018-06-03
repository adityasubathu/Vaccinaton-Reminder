package org.vaccinationreminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OffsetCalculator {

    public static String[] vaccineList = {"BCG", "OPV 0", "Hep–B 1", "DTwP 1", "IPV 1", "Hep–B 2", "Hib 1", "Rotavirus 1",
            "PCV 1", "DTwP 2", "IPV 2", "Hib 2", "Rotavirus 2", "PCV 2", "DTwP 3", "IPV 3", "Hib 3", "Rotavirus 3",
            "PCV 3", "OPV 1", "Hep–B 3", "OPV 2", "MMR 1", "TCV", "Hep–A 1", "MMR 2", "Varicella 1",
            "PCV booster", "DTwP B1/DTaP B1", "IPV B1", "Hib B1", "Hep–A 2", "Booster of Typhoid", "Conjugate Vaccine",
            "DTwP B2/DTaP B2", "OPV 3", "Varicella 2", "MMR 3", "Tdap/Td", "HPV"}; //Array of vaccines, in chronological order

    public static int[] weekList = {0, 0, 0, 6, 6, 6, 6, 6, 6, 10, 10, 10, 10, 10, 14, 14, 14, 14, 14, 26, 26, 36, 36,
            52, 52, 60, 60, 60, 72, 72, 72, 72, 104, 104, 208, 208, 208, 208, 520, 520}; //Array for number of weeks each of the above vaccine is due

    private int DOByear, DOBweekOfYear;
    private static int offset = 0, currentOffset;

    public List<String> nextVaccines;

    long getNextDate(String dateOfBirth) {

        nextVaccines = new ArrayList<>();

        Calendar DOBCal = Calendar.getInstance();
        Calendar currentDateCal = Calendar.getInstance();

        currentDateCal.setTimeInMillis(System.currentTimeMillis());
        int currentWeekOfYear = currentDateCal.get(Calendar.WEEK_OF_YEAR);
        int currentYear = currentDateCal.get(Calendar.YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date mDate = sdf.parse(dateOfBirth);

            DOBCal.setTime(mDate);
            DOBweekOfYear = DOBCal.get(Calendar.WEEK_OF_YEAR);
            DOByear = DOBCal.get(Calendar.YEAR);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (currentYear > DOByear) {

            int yearDIff = currentYear - DOByear;

            currentOffset = ((52 * yearDIff) - DOBweekOfYear) + currentWeekOfYear;

        } else if (currentYear == DOByear) {

            currentOffset = currentWeekOfYear - DOBweekOfYear;

        }

        for (int aWeekList : weekList) {

            if (aWeekList > currentOffset) {

                offset = aWeekList;
                break;

            }
        }

        for (int i = 0; i < weekList.length; i++) {

            if (weekList[i] == offset) {

                nextVaccines.add(vaccineList[i]);

            }

        }

        int newWeek = DOBweekOfYear + offset;

        int nextYear = newWeek/52;

        int nextWeek = (newWeek - (52 * nextYear));

        DOBCal.set(Calendar.WEEK_OF_YEAR, nextWeek);

        if (nextYear > 0) {

            int newYear = DOBCal.get(Calendar.YEAR);
            ++newYear;
            DOBCal.set(Calendar.YEAR, newYear);

        }


        return (DOBCal.getTimeInMillis());

    }

}
