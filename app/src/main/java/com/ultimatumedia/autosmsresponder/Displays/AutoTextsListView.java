package com.ultimatumedia.autosmsresponder.Displays;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ultimatumedia.autosmsresponder.Database.SMSMessageDatasource;
import com.ultimatumedia.autosmsresponder.R;
import com.ultimatumedia.autosmsresponder.SMS.SMSMessage;

import java.util.ArrayList;

public class AutoTextsListView extends Fragment {
    private static final String ARG_NUMBER_ID = "NUMBER_ID";

    private String lookUpNumberid;
    private ListView autoTextMessagesListView;

    public static AutoTextsListView newInstance(String lookUpNumberId) {
        AutoTextsListView fragment = new AutoTextsListView();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER_ID, lookUpNumberId);
        fragment.setArguments(args);
        return fragment;
    }

    public AutoTextsListView() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lookUpNumberid = getArguments().getString(ARG_NUMBER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_texts_list_view, container, false);
        autoTextMessagesListView = (ListView) view.findViewById(R.id.AutoTextMessages);

        Log.i("INFO (Erik)", "Checking for Test Messages in the database");
        SMSMessageDatasource smsDataSource = new SMSMessageDatasource(view.getContext());
        smsDataSource.open();
        Log.i("INFO (Erik)", "Getting messages from database");
        ArrayList<SMSMessage> messages = smsDataSource.getMessageWithPhoneId(Long.parseLong(lookUpNumberid));
        smsDataSource.close();

        ArrayList<String> toStringMessages = new ArrayList<String>();
        for(SMSMessage message : messages) {
            toStringMessages.add(message.conditionMessage + "--" + message.message);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, toStringMessages);
        autoTextMessagesListView.setAdapter(adapter);
        return view;
    }
}
