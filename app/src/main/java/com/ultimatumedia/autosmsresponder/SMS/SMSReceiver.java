package com.ultimatumedia.autosmsresponder.SMS;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.ultimatumedia.autosmsresponder.Database.NumberDatasource;

import java.util.ArrayList;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");

            if (pdus != null) {
                messages = new SmsMessage[pdus.length];

                Log.i("INFO (Erik)", "Message has been recived and is starting to process the data");


                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    String phoneNumber = messages[i].getOriginatingAddress();
                    String messageBody = messages[i].getMessageBody();

                    strMessage += "SMS From: " + phoneNumber;
                    strMessage += " : ";
                    strMessage += messageBody;
                    strMessage += "\n";
                    Log.i("INFO (Erik)", strMessage);

                    //TODO:Here is where i need to check the conditions and all that for the person
                    Log.i("INFO (Erik)", "Getting data from database for the number and the message");
                    SMSGetData smsGetData = new SMSGetData();
                    SMSMessage smsMessage = smsGetData.getData(context, phoneNumber, messageBody);

                    Log.i("INFO (Erik)", "Checking to see if there is a message to send");
                    if (smsMessage != null) {
                        Log.i("INFO (Erik)", "There is a message to send");
                        SMSSender smsSender = new SMSSender();
                        smsMessage.updateTimeStamp(context);
                        //smsSender.sendSMS(context, phoneNumber, smsMessage.message);
                    }else {
                        Log.i("INFO (Erik)", "There is not a message to send");
                    }
                }
            }
            Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
        }
        String actionName = intent.getAction();

        if (actionName.equals("com.ultimatumedia.sent")) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "Message is sent successfully", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "Error in sending Message", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        if (actionName.equals("com.ultimatumedia.delivered")) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "Message is delivered", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "Error in the delivery of message", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }
}
