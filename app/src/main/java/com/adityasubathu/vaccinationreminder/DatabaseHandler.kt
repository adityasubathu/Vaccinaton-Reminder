package com.adityasubathu.vaccinationreminder

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler internal constructor(context: Context?, username: String) {
    private val myHelper: MyDbHelper
    fun insertData(name: String?, dob: String?, gender: String?): Long {
        myHelper.createNewTable()
        val dbb = myHelper.writableDatabase //opening database
        val contentValues = ContentValues()
        contentValues.put(MyDbHelper.NAME, name)
        contentValues.put(MyDbHelper.DATE_OF_BIRTH, dob)
        contentValues.put(MyDbHelper.GENDER, gender)
        return dbb.insert(MyDbHelper.TABLE_NAME, null, contentValues)
    }

    val data: String
        get() {
            myHelper.createNewTable()
            val db = myHelper.readableDatabase
            val columns = arrayOf(MyDbHelper.UID, MyDbHelper.NAME, MyDbHelper.DATE_OF_BIRTH, MyDbHelper.GENDER)
            @SuppressLint("Recycle") val cursor = db.query(MyDbHelper.TABLE_NAME, columns, null, null, null, null, null)
            val buffer = StringBuilder()
            while (cursor.moveToNext()) {
                val cid = cursor.getInt(cursor.getColumnIndex(MyDbHelper.UID))
                val name = cursor.getString(cursor.getColumnIndex(MyDbHelper.NAME))
                val dob = cursor.getString(cursor.getColumnIndex(MyDbHelper.DATE_OF_BIRTH))
                val gender = cursor.getString(cursor.getColumnIndex(MyDbHelper.GENDER))
                buffer.append(cid).append("  ").append(name).append("  ").append(dob).append("  ").append(gender).append("  ")
            }
            return buffer.toString()
        }

    fun delete(childName: String) {
        myHelper.createNewTable()
        val db = myHelper.writableDatabase
        val whereArgs = arrayOf(childName)
        db.delete(MyDbHelper.TABLE_NAME, MyDbHelper.NAME + " = ?", whereArgs)
    }

    internal class MyDbHelper
    (context: Context?, private val TBLE_NAME: String) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_Version) {
        override fun onCreate(db: SQLiteDatabase) {
            TABLE_NAME = TBLE_NAME
            try {
                createNewTable()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            DATABASE_Version = newVersion
        }

        fun createNewTable() {
            val command = "CREATE TABLE IF NOT EXISTS $TBLE_NAME ($UID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME VARCHAR(255) ,$DATE_OF_BIRTH VARCHAR(225),$GENDER VARCHAR(255));"
            val db = writableDatabase
            db.execSQL(command)
        }

        companion object {
            lateinit var TABLE_NAME: String
            const val DATABASE_NAME = "vaccinationReminder.db" // Database Name
            var DATABASE_Version = 1 // Database Version
            const val UID = "_id" // Column I (Primary Key)
            const val NAME = "Name" // Column II
            const val DATE_OF_BIRTH = "DOB" // Column III
            const val GENDER = "Gender" // Column IV
        }

    }

    init {
        myHelper = MyDbHelper(context, username)
    }
}