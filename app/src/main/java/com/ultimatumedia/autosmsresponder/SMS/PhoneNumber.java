package com.ultimatumedia.autosmsresponder.SMS;

/**
 * Created by erik on 4/20/15.
 */
public class PhoneNumber {
    public long numberId = -1;
    public String number = "";

    @Override
    public String toString() {
        return numberId + " -- " +  number;
    }
}
