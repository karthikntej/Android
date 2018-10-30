package com.example.karthik.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "creditManager";
    private static final String TABLE_BILL = "bill";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_EXACTTIME = "exacttime";
    private static final String KEY_PLACE = "place";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREDIT_DETAILS_TABLE = "CREATE TABLE " + TABLE_BILL + "("
                + KEY_AMOUNT + " TEXT ," + KEY_PLACE + " TEXT," + KEY_YEAR + " TEXT," + KEY_MONTH + " TEXT,"
                + KEY_DAY + " TEXT," + KEY_EXACTTIME + " TEXT"  + ")";

        db.execSQL(CREDIT_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void addEntry(Context context, Detail detail) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, detail.amount);
        values.put(KEY_PLACE, detail.place);
        values.put(KEY_YEAR, detail.year);
        values.put(KEY_MONTH, detail.month);
        values.put(KEY_DAY, detail.day);
        values.put(KEY_EXACTTIME, detail.exactTime);


        db.insert(TABLE_BILL, null, values);
        Log.d("testmsg", "check point 2");
        db.close(); // Closing database connection
    }

    public List<Detail> getAllDetails(int num) {

        List<Detail> detailsList = new ArrayList<Detail>();

        String condition = " = " + num;

        String selectQuery = "SELECT  * FROM " + TABLE_BILL + " WHERE " + KEY_MONTH  + condition ;
        Log.d("testmsg", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToLast()) {
            do {
                Detail detail = new Detail();
                detail.amount = cursor.getString(0);
                detail.place  = cursor.getString(1);
                detail.year   = cursor.getInt(2);
                detail.month  = cursor.getInt(3);
                detail.day    = cursor.getInt(4);
                detail.exactTime  = cursor.getString(5);

                detailsList.add(detail);

            } while (cursor.moveToPrevious());


        }

        Log.d("testmsg", "Size of details list is " + detailsList.size());
        return detailsList;
    }

    public List getMonths() {
        List<String> months = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();


        String monthsQuery = "SELECT " + KEY_MONTH +" FROM " + TABLE_BILL
                + " GROUP BY " + KEY_MONTH + " ORDER BY " + KEY_MONTH ;
        Cursor cursor = db.rawQuery(monthsQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Log.d("testmsg", "values are " + cursor.getString(0));
                months.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Log.d("testmsg", "in method " + months.size());
        return months;
    }
}
