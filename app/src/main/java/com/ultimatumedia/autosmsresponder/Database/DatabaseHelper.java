package com.ultimatumedia.autosmsresponder.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by erik on 4/19/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";

    public static final String TABLE_NAME_SMS_MESSAGE = "messages";
    public static final String SMS_MESSAGE_ID = "id";
    public static final String SMS_MESSAGE_CONDITION_MESSAGE = "conditionMessage";
    public static final String SMS_MESSAGE_MESSAGE = "message";
    public static final String SMS_MESSAGE_TIME_STAMP = "timeStamp";
    public static final String SMS_MESSAGE_PARENT_MESSAGE_ID = "parentMessageId";
    public static final String SMS_MESSAGE_NUMBER_ID = "numberId";

    public static final String TABLE_NAME_NUMBERS = "numbers";
    public static final String NUMBER_ID = "id";
    public static final String NUMBER_NUMBER = "number";

    private static final String SQL_CREATE_SMS_MESSAGE_TABLE =
            "CREATE TABLE " + TABLE_NAME_SMS_MESSAGE + " (" +
                    SMS_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SMS_MESSAGE_CONDITION_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    SMS_MESSAGE_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    SMS_MESSAGE_NUMBER_ID + INT_TYPE + COMMA_SEP +
                    //"FOREIGN KEY ("+SMS_MESSAGE_NUMBER_ID+") REFERENCES "+TABLE_NAME_NUMBERS+" ("+NUMBER_ID+"), " +
                    SMS_MESSAGE_PARENT_MESSAGE_ID + INT_TYPE + COMMA_SEP +
                    //"FOREIGN KEY ("+SMS_MESSAGE_PARENT_MESSAGE_ID+") REFERENCES "+TABLE_NAME_SMS_MESSAGE+" ("+SMS_MESSAGE_ID+"), " +
                    SMS_MESSAGE_TIME_STAMP + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY ("+SMS_MESSAGE_NUMBER_ID+") REFERENCES "+TABLE_NAME_NUMBERS+" ("+NUMBER_ID+")" +
                    ")";

    private static final String SQL_CREATE_NUMBER_TABLE = "CREATE TABLE " + TABLE_NAME_NUMBERS + " (" +
            NUMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NUMBER_NUMBER + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_NUMBER_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_NUMBERS;

    private static final String SQL_DELETE_SMS_MESSAGE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_SMS_MESSAGE;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "node.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NUMBER_TABLE);
        db.execSQL(SQL_CREATE_SMS_MESSAGE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_SMS_MESSAGE_TABLE);
        db.execSQL(SQL_DELETE_NUMBER_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
