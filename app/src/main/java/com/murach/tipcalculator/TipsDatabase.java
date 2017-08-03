package com.murach.tipcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Admin on 7/16/2017.
 */

public class TipsDatabase extends SQLiteOpenHelper{

    //define DB variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tips.db";
    public static final String TABLE_NAME = "tips";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BILL_DATE = "billDate";
    public static final String COLUMN_BILL_AMOUNT = "billAmount";
    public static final String COLUMN_TIP_PERCENT = "tipPercent";

    private SQLiteDatabase database;

    public TipsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //responsible for creating a table for the first time
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE "+TABLE_NAME+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_BILL_DATE+" INTEGER NOT NULL, "+COLUMN_BILL_AMOUNT+" REAL NOT NULL, "+
                COLUMN_TIP_PERCENT+" REAL NOT NULL);";
        sqLiteDatabase.execSQL(query);
        addBill(0, 40.60, 1.3f);
        addBill(0, 35.20, 2);
    }

    //responsible for making updates on an existing table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public TipsDatabase open() throws SQLException {
        database = getWritableDatabase(); //get reference to the database
        return this;
    }

    //add new row to the database
    public void addBill(int date, double bill, float tipPercent) {
        //content values is built into android that allows you to add several values in one statement
        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_DATE, date);
        values.put(COLUMN_BILL_AMOUNT, bill);
        values.put(COLUMN_TIP_PERCENT, tipPercent);
        //call the open method to get reference to the database
        open();
        database.insert(TABLE_NAME, null, values);

        //once you are done with the database, close it out to give memory back
        close();
    }

    public ArrayList<String> getItem() {
        ArrayList<String> tipAL = new ArrayList<String>();
        open();
        Cursor c = readEntry();

        int rows = c.getCount();
        int columns = c.getColumnCount();

        c.moveToFirst();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; i++) {
                String value = c.getString(j);
                tipAL.add(value);
            }
            c.moveToNext();
        }

        return tipAL;
    }

    public Cursor readEntry() {
        String[] allColumns = new String[] {
                COLUMN_ID, COLUMN_BILL_AMOUNT, COLUMN_TIP_PERCENT, COLUMN_BILL_DATE
        };
        Cursor c = database.query(TABLE_NAME, allColumns, null, null, null, null, null);
        if(c!=null) {
            c.moveToFirst();
        }
        return c;
    }

}
