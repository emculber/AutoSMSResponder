package com.ultimatumedia.autosmsresponder.SMS;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");

            if (pdus != null) {
                messages = new SmsMessage[pdus.length];


                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    strMessage += "SMS From: " + messages[i].getOriginatingAddress();
                    strMessage += " : ";
                    strMessage += messages[i].getMessageBody();
                    strMessage += "\n";

                    //TODO:Here is where i need to check the conditions and all that for the person
                    if (messages[i].getMessageBody().equalsIgnoreCase("Test")) {
                        SMSSender smsSender = new SMSSender();
                        smsSender.sendSMS(context, messages[i].getOriginatingAddress(), "Success!!");
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
