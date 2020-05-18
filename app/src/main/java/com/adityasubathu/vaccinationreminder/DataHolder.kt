package com.adityasubathu.vaccinationreminder

import android.content.Context
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DataHolder {
    //Array of vaccines, in chronological order
    @JvmField
    var vaccineList: List<String> = listOf("BCG, OPV 0, Hep–B 1", "DTwP 1, IPV 1, Hep–B 2, Hib 1, Rotavirus 1, PCV 1", "DTwP 2, IPV 2, Hib 2, Rotavirus 2, PCV 2", "DTwP 3, IPV 3, Hib 3, Rotavirus 3, PCV 3", "OPV 1, Hep–B 3", "OPV 2, MMR 1", "TCV, Hep–A 1", "MMR 2, Varicella 1, PCV booster", "DTwP B1/DTaP B1, IPV B1, Hib B1, Hep–A 2", "Booster of Typhoid, Conjugate Vaccine", "DTwP B2/DTaP B2, OPV 3, Varicella 2, MMR 3", "Tdap/Td, HPV")

    //Array for number of weeks each of the above vaccine is due
    @JvmField
    var weekList: List<Int> = listOf(1, 6, 10, 14, 26, 36, 52, 60, 72, 104, 208, 520)
    private var nextVaccines: String? = null
    fun getVaccineList(context: Context?, position: Int, appendString: String): String? {
        val offset: Int
        val listCreator = ListCreator()
        val dOBList = listCreator.getDOBList(context)
        val dateOfBirth = dOBList[position]
        offset = getOffset(dateOfBirth)
        for (i in weekList.indices) {
            if (weekList[i] == offset) {
                nextVaccines = vaccineList[i]
            }
        }
        return if (appendString == ", ") {
            nextVaccines
        } else {
            val vaccineListStringBuilder = StringBuilder()
            val brokenVaccineList = nextVaccines!!.split(", ").toTypedArray()
            for (j in brokenVaccineList.indices) {
                if (j == brokenVaccineList.size - 1) {
                    vaccineListStringBuilder.append(brokenVaccineList[j]).append("")
                } else {
                    vaccineListStringBuilder.append(brokenVaccineList[j]).append(appendString)
                }
            }
            vaccineListStringBuilder.toString()
        }
    }

    fun getNextVaccineDate(dateOfBirth: String): Long {
        val offset: Int = getOffset(dateOfBirth)
        if (offset > 520) {
            return 1
        }
        val nextVaccineDateCalendar = Calendar.getInstance()
        nextVaccineDateCalendar.time = DOB
        val year = offset / 52
        val weeks = offset - 52 * year
        nextVaccineDateCalendar.add(Calendar.YEAR, year)
        nextVaccineDateCalendar.add(Calendar.WEEK_OF_YEAR, weeks)
        return nextVaccineDateCalendar.timeInMillis
    }

    private fun getOffset(dateOfBirth: String): Int {
        var weeks = 0
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeNow = Calendar.getInstance()
        timeNow.timeInMillis = System.currentTimeMillis()
        val dOBCalendar = Calendar.getInstance()
        try {
            DOB = sdf.parse(dateOfBirth)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        dOBCalendar.time = DOB
        val dobInMillis = dOBCalendar.timeInMillis
        val elapsedTimeSinceBirthInMillis = System.currentTimeMillis() - dobInMillis
        val elapsedTimeSinceBirthInWeeks = (elapsedTimeSinceBirthInMillis / 1000 / 60 / 60 / 24 / 7).toInt()
        for (weeksInterval in weekList) {
            if (weeksInterval > elapsedTimeSinceBirthInWeeks) {
                weeks = weeksInterval
                break
            }
        }
        return if (elapsedTimeSinceBirthInWeeks > 520) {
            521
        } else weeks
    }

    companion object {
        private lateinit var DOB: Date
    }
}