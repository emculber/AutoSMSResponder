package com.ultimatumedia.autosmsresponder.SMS;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.ultimatumedia.autosmsresponder.MainActivity;


/**
 * Created by erik on 4/19/15.
 */
public class SMSSender {

    public void sendSMS(Context context, String phoneNumber, String message) {
        /** Creating a pending intent which will be broadcasted when an sms message is successfully sent */
        PendingIntent piSent = PendingIntent.getBroadcast(context, 0, new Intent("com.ultimatumedia.sent") , 0);

        /** Creating a pending intent which will be broadcasted when an sms message is successfully delivered */
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, new Intent("com.ultimatumedia.delivered"), 0);

        /** Getting an instance of SmsManager to sent sms message from the application*/
        SmsManager smsManager = SmsManager.getDefault();

        /** Sending the Sms message to the intended party */
        smsManager.sendTextMessage(phoneNumber, null, message, piSent, piDelivered);
    }
}
