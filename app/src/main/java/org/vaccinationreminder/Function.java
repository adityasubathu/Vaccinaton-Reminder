package org.vaccinationreminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Function {

    private String[] vaccineList = {"BCG", "OPV 0", "Hep–B 1", "DTwP 1", "IPV 1", "Hep–B 2", "Hib 1", "Rotavirus 1",
            "PCV 1", "DTwP 2", "IPV 2", "Hib 2", "Rotavirus 2", "PCV 2", "DTwP 3", "IPV 3", "Hib 3", "Rotavirus 3",
            "PCV 3", "OPV 1", "Hep–B 3", "MMR 1", "Typhoid Conjugate Vaccine", "Hep–A 1", "MMR 2", "Varicella 1",
            "PCV booster", "DTwP B1/DTaP B1", "IPV B1", "Hib B1", "Hep–A 2", "Booster of Typhoid", "Conjugate Vaccine",
            "DTwP B2/DTaP B2", "Varicella 2", "MMR 3", "Tdap/Td", "HPV"};
    
    private int[] weekList = {0, 0, 0, 0, 0, 6, 6, 6, 6, 6, 6, 10, 10, 10, 10, 10, 10, 10, 10, 10, 26, 26, 38,
            52, 52, 64, 64, 64, 78, 78, 78, 78, 104, 104, 216, 216, 216, 216, 520};

    private long getNextDate(int dateOfBirth) {

        int offset = 0;

        Calendar cal = Calendar.getInstance();

        int nextDate = cal.get(Calendar.WEEK_OF_YEAR + offset);

            int week = nextDate/52;

            nextDate = cal.get(nextDate - (52 * week));

            cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) + week);

        cal.set(Calendar.WEEK_OF_YEAR, nextDate);

        return (cal.getTimeInMillis());

    }

}
