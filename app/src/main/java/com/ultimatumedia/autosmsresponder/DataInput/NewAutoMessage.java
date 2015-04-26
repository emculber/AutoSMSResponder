package com.ultimatumedia.autosmsresponder.DataInput;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ultimatumedia.autosmsresponder.Adapters.NewAutoMessageSpinnerBaseAdapter;
import com.ultimatumedia.autosmsresponder.R;

import org.w3c.dom.Text;

public class NewAutoMessage extends Fragment {
    public NewAutoMessage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_auto_message, container, false);

        final Button newMessageSpecificTime = (Button) view.findViewById(R.id.NewMessageSpecificTime);
        final TextView newMessageMessageContain = (TextView) view.findViewById(R.id.NewMessageMessageContain);
        newMessageSpecificTime.setVisibility(View.INVISIBLE);
        newMessageMessageContain.setVisibility(View.INVISIBLE);

        Spinner conditionMessageSpinner = (Spinner) view.findViewById(R.id.NewMessageConditionMessageSpinner);
        NewAutoMessageSpinnerBaseAdapter adapter = new NewAutoMessageSpinnerBaseAdapter(view.getContext());
        conditionMessageSpinner.setAdapter(adapter);

        conditionMessageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedItem = (TextView) view.findViewById(R.id.SpinnerItem);

                if(selectedItem.getText().toString().equalsIgnoreCase("When Message Contains")) {
                    newMessageMessageContain.setVisibility(View.VISIBLE);
                    newMessageSpecificTime.setVisibility(View.INVISIBLE);
                }else if(selectedItem.getText().toString().equalsIgnoreCase("Specific Time")) {
                    newMessageSpecificTime.setVisibility(View.VISIBLE);
                    newMessageMessageContain.setVisibility(View.INVISIBLE);
                }else {
                    newMessageSpecificTime.setVisibility(View.INVISIBLE);
                    newMessageMessageContain.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void addNewMessage() {

    }
}