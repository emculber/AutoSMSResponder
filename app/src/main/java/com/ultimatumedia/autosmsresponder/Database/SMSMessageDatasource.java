package com.ultimatumedia.autosmsresponder.Database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ultimatumedia.autosmsresponder.ResponseTree.Node;
import com.ultimatumedia.autosmsresponder.SMS.PhoneNumber;
import com.ultimatumedia.autosmsresponder.SMS.SMSMessage;

import java.util.ArrayList;

/**
 * Created by erik on 4/20/15.
 */
public class SMSMessageDatasource {

    public final String[] columns = {
            DatabaseHelper.SMS_MESSAGE_ID,
            DatabaseHelper.SMS_MESSAGE_CONDITION_MESSAGE,
            DatabaseHelper.SMS_MESSAGE_MESSAGE,
            DatabaseHelper.SMS_MESSAGE_NUMBER_ID,
            DatabaseHelper.SMS_MESSAGE_PARENT_MESSAGE_ID,
            DatabaseHelper.SMS_MESSAGE_TIME_STAMP };

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

    public SMSMessageDatasource(Context context) {
        Log.i("INFO (Erik)", "New Instance of Datasource");
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        Log.i("INFO (Erik)", "Opening database for the Message Database.");
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        Log.i("INFO (Erik)", "Closing database for the Message Database.");
        dbHelper.close();
    }

    public SMSMessage create(SMSMessage message) {
        Log.i("INFO (Erik)", "Creating new message index into the message Database.");
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SMS_MESSAGE_CONDITION_MESSAGE, message.conditionMessage);
        values.put(DatabaseHelper.SMS_MESSAGE_MESSAGE, message.message);
        values.put(DatabaseHelper.SMS_MESSAGE_NUMBER_ID, message.numberId);
        values.put(DatabaseHelper.SMS_MESSAGE_PARENT_MESSAGE_ID, message.parentMessageId);
        values.put(DatabaseHelper.SMS_MESSAGE_TIME_STAMP, message.getDateTime());
        Log.i("INFO (Erik)", "message being added:" + message.toString());
        long smsMessageId = database.insert(DatabaseHelper.TABLE_NAME_SMS_MESSAGE, null, values);
        Log.i("INFO (Erik)", "Message was added with the id:" + smsMessageId);
        message.smsMessageId = smsMessageId;
        Log.i("INFO (Erik)", "Returning the Message with the id");
        return message;
    }

    public ArrayList<SMSMessage> getMessages() {
        Log.i("INFO (Erik)", "Getting the total list of messages from the messages database");
        ArrayList<SMSMessage> messages = new ArrayList<SMSMessage>();

        Log.i("INFO (Erik)", "Quering data from the messages database");
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_SMS_MESSAGE, columns, null, null, null, null, null);
        Log.i("INFO (Erik)", cursor.getCount() + " Items were quered from the message database");
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                SMSMessage smsMessage = new SMSMessage();
                smsMessage.smsMessageId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_ID));
                smsMessage.conditionMessage = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_CONDITION_MESSAGE));
                smsMessage.message = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_MESSAGE));
                smsMessage.numberId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_NUMBER_ID));
                smsMessage.parentMessageId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_PARENT_MESSAGE_ID));
                smsMessage.setDateTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_TIME_STAMP)));
                Log.i("INFO (Erik)", "Adding " + smsMessage.toString() + " to the list");
                messages.add(smsMessage);
            }
        }
        Log.i("INFO (Erik)", "Returning the list of messages");
        return messages;
    }

    public ArrayList<SMSMessage> getMessageWithPhoneId(long phoneId) {
        Log.i("INFO (Erik)", "Getting the a list of messages with phone id" + phoneId + " from the message database");
        ArrayList<SMSMessage> messages = new ArrayList<SMSMessage>();

        String where = DatabaseHelper.SMS_MESSAGE_NUMBER_ID + "=?";
        String[] whereArgs = new String[]{Long.toString(phoneId)};
        Log.i("INFO (Erik)", "Quering data from the message database");
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_SMS_MESSAGE, columns, where, whereArgs, null, null, null);
        Log.i("INFO (Erik)", cursor.getCount() + " Items were quered from the message database");
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                SMSMessage smsMessage = new SMSMessage();
                smsMessage.smsMessageId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_ID));
                smsMessage.conditionMessage = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_CONDITION_MESSAGE));
                smsMessage.message = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_MESSAGE));
                smsMessage.numberId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_NUMBER_ID));
                smsMessage.parentMessageId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_PARENT_MESSAGE_ID));
                smsMessage.setDateTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SMS_MESSAGE_TIME_STAMP)));
                Log.i("INFO (Erik)", "Adding " + smsMessage.toString() + " to the list");
                messages.add(smsMessage);
            }
        }
        Log.i("INFO (Erik)", "Returning the list of messages");
        return messages;
    }

    public void updateTimeStamp(SMSMessage message) {
        String where = DatabaseHelper.SMS_MESSAGE_ID + "=" + message.smsMessageId;
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SMS_MESSAGE_TIME_STAMP, message.getDateTime());
        database.update(DatabaseHelper.TABLE_NAME_SMS_MESSAGE, values, where, null);
    }

    public boolean deleteMessages(SMSMessage message) {
        Log.i("INFO (Erik)", "Deleting the a Message " + message.smsMessageId + " from the message database");
        String where = DatabaseHelper.SMS_MESSAGE_ID + "=?";
        int result = database.delete(DatabaseHelper.TABLE_NAME_SMS_MESSAGE, where, new String[] {Long.toString(message.smsMessageId)});
        Log.i("INFO (Erik)", "Returning weather the number was deleted or not (" + (result == 1) + ")");
        return (result == 1);
    }
}
