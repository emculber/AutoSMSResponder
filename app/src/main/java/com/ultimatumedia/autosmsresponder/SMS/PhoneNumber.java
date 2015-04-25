package com.ultimatumedia.autosmsresponder.SMS;

/**
 * Created by erik on 4/20/15.
 */
public class PhoneNumber {
    public long numberId = -1;
    public String name = "";
    public String number = "";
    public String active = "T";

    @Override
    public String toString() {
        return numberId + " -- " + name + "--" +  number;
    }
}
