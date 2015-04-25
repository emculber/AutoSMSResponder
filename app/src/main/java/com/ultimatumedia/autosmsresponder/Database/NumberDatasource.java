package com.ultimatumedia.autosmsresponder.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ultimatumedia.autosmsresponder.SMS.PhoneNumber;

import java.util.ArrayList;

/**
 * Created by erik on 4/20/15.
 */
public class NumberDatasource {
    public final String[] columns = {
            DatabaseHelper.NUMBER_ID,
            DatabaseHelper.NUMBER_NAME,
            DatabaseHelper.NUMBER_NUMBER,
            DatabaseHelper.NUMBER_ACTIVE };

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

    private boolean datasourceOpen = false;

    public NumberDatasource(Context context) {
        Log.i("INFO (Erik)", "New Instance of Datasource");
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        Log.i("INFO (Erik)", "Opening database for the Number Database.");
        if(!datasourceOpen) {
            database = dbHelper.getWritableDatabase();
            datasourceOpen = true;
        }
    }

    public void close() {
        Log.i("INFO (Erik)", "Closing database for the Number Database.");
        if(datasourceOpen) {
            dbHelper.close();
            datasourceOpen = false;
        }
    }

    public PhoneNumber create(PhoneNumber number) {
        Log.i("INFO (Erik)", "Creating new number index into the Number Database.");
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NUMBER_NAME, number.name);
        values.put(DatabaseHelper.NUMBER_NUMBER, number.number);
        values.put(DatabaseHelper.NUMBER_ACTIVE, number.active);
        Log.i("INFO (Erik)", "Number being added:" + number.number);
        long numberID = database.insert(DatabaseHelper.TABLE_NAME_NUMBERS, null, values);
        Log.i("INFO (Erik)", "Number was added with the id:" + numberID);
        number.numberId = numberID;
        Log.i("INFO (Erik)", "Returning the number with the id");
        return number;
    }

    public ArrayList<PhoneNumber> getNumbers() {
        Log.i("INFO (Erik)", "Getting the total list of number from the number database");
        ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();

        Log.i("INFO (Erik)", "Quering data from the number database");
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_NUMBERS, columns, null, null, null, null, null);
        Log.i("INFO (Erik)", cursor.getCount() + " Items were quered from the number database");
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                PhoneNumber number = new PhoneNumber();
                number.numberId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.NUMBER_ID));
                number.name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMBER_NAME));
                number.number = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMBER_NUMBER));
                number.active = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMBER_ACTIVE));
                Log.i("INFO (Erik)", "Adding " + number.number + " with the id " + number.numberId + " to the list");
                numbers.add(number);
            }
        }
        Log.i("INFO (Erik)", "Returning the list of numbers with there ids");
        return numbers;
    }


    public PhoneNumber getNumber(String number) {
        Log.i("INFO (Erik)", "Getting the a single number " + number + " from the number database");
        PhoneNumber phoneNumber = new PhoneNumber();

        String where = DatabaseHelper.NUMBER_NUMBER + "=?";
        String[] whereArgs = new String[]{number};

        Log.i("INFO (Erik)", "Quering data from the number database");
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_NUMBERS, columns, where, whereArgs, null, null, null);
        Log.i("INFO (Erik)", cursor.getCount() + " Items were quered from the number database");
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                //TODO:Check for multiple numbers. make numbers a unique column maybe
                phoneNumber.numberId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.NUMBER_ID));
                phoneNumber.name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMBER_NAME));
                phoneNumber.number = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMBER_NUMBER));
                phoneNumber.active = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NUMBER_ACTIVE));
                Log.i("INFO (Erik)", "Getting " + phoneNumber.number + " with the id " + phoneNumber.numberId);
            }
        }
        Log.i("INFO (Erik)", "Returning the number and id");
        return phoneNumber;
    }

    public void updateActive(PhoneNumber number) {
        String where = DatabaseHelper.NUMBER_ID + "=" + number.numberId;
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NUMBER_ACTIVE, number.active);
        database.update(DatabaseHelper.TABLE_NAME_NUMBERS, values, where, null);
    }

    public boolean deleteNumber(PhoneNumber number) {
        Log.i("INFO (Erik)", "Deleting the a number " + number + " from the number database");
        String where = DatabaseHelper.NUMBER_ID + "=?";
        int result = database.delete(DatabaseHelper.TABLE_NAME_NUMBERS, where, new String[] {Long.toString(number.numberId)});
        Log.i("INFO (Erik)", "Returning weather the number was deleted or not (" + (result == 1) + ")");
        return (result == 1);
    }
}
