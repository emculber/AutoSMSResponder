package com.ultimatumedia.autosmsresponder.SMS;

import android.content.Context;
import android.util.Log;

import com.ultimatumedia.autosmsresponder.Database.SMSMessageDatasource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by erik on 4/20/15.
 */
public class SMSMessage {
    public long smsMessageId = -1;
    public String conditionMessage = "";
    public String message = "";
    public int numberId = -1;
    public int parentMessageId = -1;
    public Date timeStamp = new Date();

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(timeStamp);
    }
    public void setDateTime(String Date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            timeStamp = dateFormat.parse(Date);
        }catch (ParseException pe) {
            Log.e("ERROR (Erik)", "PARSING ERRROR:" + pe.getMessage());
        }
    }

    public void updateTimeStamp(Context context) {
        timeStamp = new Date();
        SMSMessageDatasource smsDataSource = new SMSMessageDatasource(context);
        smsDataSource.open();
        smsDataSource.updateTimeStamp(this);
        smsDataSource.close();
    }

    public long getDateDiff(Date date, TimeUnit timeUnit) {
        long diffInMillies = date.getTime() - timeStamp.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return "SMSMessage{" +
                "smsMessageId=" + smsMessageId +
                ", conditionMessage='" + conditionMessage + '\'' +
                ", message='" + message + '\'' +
                ", numberId=" + numberId +
                ", parentMessageId=" + parentMessageId +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
