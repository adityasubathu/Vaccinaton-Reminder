package com.adityasubathu.vaccinationreminder

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class ListCreator {
    fun getDOBList(context: Context?): MutableList<String> {
        val dOBList: MutableList<String> = ArrayList()
        val helper = databaseHandler(context, LoginActivitySignUpFragment.activeUsername)
        val data = helper.data
        val arr = data.split(" {2}").toTypedArray()
        for (i in arr.indices) {
            if (isInteger(arr[i])) {
                dOBList.add(arr[i + 2])
            }
        }
        return dOBList
    }

    fun getChildrenList(context: Context): MutableList<String> {
        val childrenList: MutableList<String> = ArrayList()
        val helper = databaseHandler(context, LoginActivitySignUpFragment.activeUsername)
        val data = helper.data
        val arr = data.split(" {2}").toTypedArray()
        for (i in arr.indices) {
            if (isInteger(arr[i])) {
                childrenList.add(arr[i + 1])
            }
        }
        return childrenList
    }

    fun getGenderList(context: Context?): MutableList<String> {
        val genderList: MutableList<String> = ArrayList()
        val helper = databaseHandler(context, LoginActivitySignUpFragment.activeUsername)
        val data = helper.data
        val arr = data.split(" {2}").toTypedArray()
        for (i in arr.indices) {
            if (isInteger(arr[i])) {
                genderList.add(arr[i + 3])
            }
        }
        return genderList
    }

    fun getFullVaccineDatesList(dateOfBirth: String): MutableList<String> {
        val holder = DataHolder()
        val fullVaccineDatesList: MutableList<String> = ArrayList()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        lateinit var dOB: Date
        try {
            dOB = formatter.parse(dateOfBirth)!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = dOB
        calendar.time = dOB
        for (i in holder.weekList.indices) {
            val year = holder.weekList[i] / 52
            val weeks = holder.weekList[i] - 52 * year
            calendar.add(Calendar.YEAR, year)
            calendar.add(Calendar.WEEK_OF_YEAR, weeks)
            fullVaccineDatesList.add(formatter.format(calendar.time))
            calendar.time = dOB
        }
        calendar.time = dOB
        calendar.add(Calendar.YEAR, 10)
        return fullVaccineDatesList
    }

    companion object {
        private fun isInteger(str: String?): Boolean {
            if (str == null) {
                return false
            }
            val length = str.length
            if (length == 0) {
                return false
            }
            var i = 0
            if (str[0] == '-') {
                if (length == 1) {
                    return false
                }
                i = 1
            }
            while (i < length) {
                val c = str[i]
                if (c < '0' || c > '9') {
                    return false
                }
                i++
            }
            return true
        }
    }
}