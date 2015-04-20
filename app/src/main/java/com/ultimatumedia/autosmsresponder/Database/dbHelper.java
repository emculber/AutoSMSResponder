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
public class dbHelper extends SQLiteOpenHelper {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Inner class that defines the table contents */
    public static abstract class NodeTable implements BaseColumns {
        public static final String TABLE_NAME = "Nodes";
        public static final String COLUMN_NAME_NUMBER = "Number";
        public static final String COLUMN_NAME_CONDITON_MESSAGE = "Condition_Message";
        public static final String COLUMN_NAME_MESSAGE = "Message";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NodeTable.TABLE_NAME + " (" +
                    NodeTable._ID + " INTEGER PRIMARY KEY," +
                    NodeTable.COLUMN_NAME_NUMBER + TEXT_TYPE + COMMA_SEP +
                    NodeTable.COLUMN_NAME_CONDITON_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    NodeTable.COLUMN_NAME_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NodeTable.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Node.db";

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addData(String Number, String Conditon, String Message) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NodeTable.COLUMN_NAME_NUMBER, Number);
        values.put(NodeTable.COLUMN_NAME_CONDITON_MESSAGE, Conditon);
        values.put(NodeTable.COLUMN_NAME_MESSAGE, Message);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(NodeTable.TABLE_NAME, null, values);
    }

    public void readData() {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {NodeTable.COLUMN_NAME_NUMBER, NodeTable.COLUMN_NAME_CONDITON_MESSAGE, NodeTable.COLUMN_NAME_MESSAGE};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = NodeTable.COLUMN_NAME_NUMBER + " DESC";

        String selection = "column1 = ? OR column1 = ? OR column1 = ?";
        String[] whereArgs = new String[]{
                "value1",
                "value2"
        };

        Cursor c = db.query(NodeTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }
}
