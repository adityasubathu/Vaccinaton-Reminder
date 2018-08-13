package com.adityasubathu.vaccinationreminder;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHandler {

    private myDbHelper myHelper;

    databaseHandler(Context context) {
        myHelper = new myDbHelper(context);
    }


    public long insertData(String name, String dob, String gender) {

        SQLiteDatabase dbb = myHelper.getWritableDatabase();  //opening database
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME, name);
        contentValues.put(myDbHelper.DATE_OF_BIRTH, dob);
        contentValues.put(myDbHelper.GENDER, gender);

        return dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);

    }

    public String getData() {

        SQLiteDatabase db = myHelper.getReadableDatabase();
        String[] columns = {myDbHelper.UID, myDbHelper.NAME, myDbHelper.DATE_OF_BIRTH, myDbHelper.GENDER};

        @SuppressLint("Recycle")

        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuilder buffer = new StringBuilder();

        while (cursor.moveToNext()) {

            int cid = cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            String dob = cursor.getString(cursor.getColumnIndex(myDbHelper.DATE_OF_BIRTH));
            String gender = cursor.getString(cursor.getColumnIndex(myDbHelper.GENDER));
            buffer.append(cid).append("  ").append(name).append("  ").append(dob).append("  ").append(gender).append("  ");

        }
        return buffer.toString();

    }

    public void delete(String childname) {

        SQLiteDatabase db = myHelper.getWritableDatabase();
        String[] whereArgs = {childname};

        db.delete(myDbHelper.TABLE_NAME, myDbHelper.NAME + " = ?", whereArgs);
    }


    static class myDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "childrenDatabase";    // Database Name
        private static final String TABLE_NAME = "myTable";            // Table Name
        private static final int DATABASE_Version = 1;               // Database Version
        private static final String UID = "_id";                      // Column I (Primary Key)
        private static final String NAME = "Name";                   // Column II
        private static final String DATE_OF_BIRTH = "DOB";         // Column III
        private static final String GENDER = "Gender";            // Column IV
        private Context context;

        myDbHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {

                db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " VARCHAR(255) ," + DATE_OF_BIRTH + " VARCHAR(225)," + GENDER + " VARCHAR(255));");
            } catch (Exception e) {
                Message.message(context, "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
