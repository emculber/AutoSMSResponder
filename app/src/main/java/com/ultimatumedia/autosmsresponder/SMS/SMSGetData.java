package com.ultimatumedia.autosmsresponder.SMS;

import android.content.Context;
import android.util.Log;

import com.ultimatumedia.autosmsresponder.Database.NumberDatasource;
import com.ultimatumedia.autosmsresponder.Database.SMSMessageDatasource;

import java.util.concurrent.TimeUnit;
import java.util.Date;

/**
 * Created by erik on 4/20/15.
 */
public class SMSGetData {
    public SMSGetData() {
        Log.i("INFO (Erik)", "New Instance of SMSGetData as well as starting the process to get data");
    }
    public SMSMessage getData(Context context, String phoneNumber, String message) {
        Log.i("INFO (Erik)", "Getting Data with the phone number " + phoneNumber + " and the message " + message);
        PhoneNumber Default = null;
        PhoneNumber NumberFound = null;
        Log.i("INFO (Erik)", "Checking to see if there is a number in the database that matches the number of the sender");
        NumberDatasource dataSource = new NumberDatasource(context);
        dataSource.open();
        if(dataSource.getNumbers().size() > 0) {
            for(PhoneNumber number : dataSource.getNumbers()) {
                if(number.number.equalsIgnoreCase(phoneNumber)) {
                    Log.i("INFO (Erik)", "Number of the send was found");
                    NumberFound = number;
                }
                if(number.number.equalsIgnoreCase("5869072309")) {
                    Log.i("INFO (Erik)", "default number was found");
                    Default = number;
                }
            }
        }
        dataSource.close();

        if(NumberFound != null) {
            Log.i("INFO (Erik)", "number was found and overwritting the default value");
            Default = NumberFound;
        } else {
            Log.i("INFO (Erik)", "number was not found and using the default value");
        }

        SMSMessage smsMessage = null;
        if(Default != null) {
            SMSMessageDatasource smsDataSource = new SMSMessageDatasource(context);
            smsDataSource.open();
            if (smsDataSource.getMessageWithPhoneId(Default.numberId).size() > 0) {
                for (SMSMessage tempMessage : smsDataSource.getMessageWithPhoneId(Default.numberId)) {
                    //TODO:Here is where i need to check for the condition of the message in order to return the right one
                    Log.i("INFO (Erik)", "smsMessage was found and using that and is going to send But first check to see if it was sent recently " + tempMessage.message);
                    long timeSenceLastSent = tempMessage.getDateDiff(new Date(), TimeUnit.MINUTES);
                    Log.i("INFO (Erik)", "it was sent " + timeSenceLastSent + " minutes ago");
                    if(timeSenceLastSent > 5) {
                        smsMessage = tempMessage;
                        break;
                    }
                }
            }
            smsDataSource.close();
        }

        if(smsMessage != null) {
            Log.i("INFO (Erik)", "returning " + smsMessage.toString());
        }else {
            Log.i("INFO (Erik)", "returning null");
        }
        return smsMessage;
    }
}
