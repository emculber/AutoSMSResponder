package com.ultimatumedia.autosmsresponder.Displays;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ultimatumedia.autosmsresponder.Database.NumberDatasource;
import com.ultimatumedia.autosmsresponder.Database.SMSMessageDatasource;
import com.ultimatumedia.autosmsresponder.R;
import com.ultimatumedia.autosmsresponder.SMS.PhoneNumber;
import com.ultimatumedia.autosmsresponder.SMS.SMSMessage;

import java.util.ArrayList;

public class NumberListFragment extends Fragment {

    private NumberDatasource dataSource;

    public NumberListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number_list, container, false);

        ListView numberListView = (ListView) view.findViewById(R.id.numberList);

        Log.i("INFO (Erik)", "Checking for Test Values in database");
        ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
        dataSource = new NumberDatasource(view.getContext());
        dataSource.open();
        Log.i("INFO (Erik)", "Checking if there is anything in database");
        if (dataSource.getNumbers().size() < 1) {
            Log.i("INFO (Erik)", "Nothing in database so create a default (My Number)");
            PhoneNumber tempNumber = new PhoneNumber();
            tempNumber.number = "5869072309";
            tempNumber = dataSource.create(tempNumber);
            numbers.add(tempNumber);
        } else {
            numbers = dataSource.getNumbers();
        }

        //TEMP DATA


        Log.i("INFO (Erik)", "Checking for Test Messages in the database");
        SMSMessageDatasource smsDataSource = new SMSMessageDatasource(view.getContext());
        smsDataSource.open();
        Log.i("INFO (Erik)", "Checking if there is anything in database");
        if (smsDataSource.getMessages().size() < 1) {
            Log.i("INFO (Erik)", "Nothing in database so create a default Message");
            SMSMessage message = new SMSMessage();

            message.conditionMessage = "no contion at this time";
            message.message = "This is an Automated Response!";
            message.numberId = (int) dataSource.getNumber(numbers.get(0).number).numberId;
            message.parentMessageId = 0;
            smsDataSource.create(message);
        }
        smsDataSource.close();
        dataSource.close();

        //TEMP DATA


        ArrayList<String> toStringNumber = new ArrayList<String>();
        for (PhoneNumber number : numbers) {
            toStringNumber.add(number.toString());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, toStringNumber);

        numberListView.setAdapter(arrayAdapter);
        return view;
    }
}