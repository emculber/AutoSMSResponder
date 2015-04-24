package com.ultimatumedia.autosmsresponder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.view.Menu;
import android.view.MenuItem;

import com.ultimatumedia.autosmsresponder.Database.NumberDatasource;
import com.ultimatumedia.autosmsresponder.Displays.AutoTextsListView;
import com.ultimatumedia.autosmsresponder.Displays.NumberListFragment;
import com.ultimatumedia.autosmsresponder.ResponseTree.ResponseTree;
import com.ultimatumedia.autosmsresponder.SMS.PhoneNumber;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static MainActivity mainActivity;
    private ArrayList<String> Contancts;
    private AutoCompleteTextView textView;
    private NumberListFragment numberListFragment;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        Contancts = new ArrayList<String>();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contancts.add(name + "--" + phoneNumber);
        }
        phones.close();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        // actionBar.setDisplayShowTitleEnabled(false);
        // actionBar.setIcon(R.drawable.ic_action_search);

        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar, null);

        actionBar.setCustomView(v);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Contancts);
        textView = (AutoCompleteTextView) v
                .findViewById(R.id.editText1);
        textView.setAdapter(adapter);


        ResponseTree responseTree = new ResponseTree();
        numberListFragment = new NumberListFragment();
        ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, numberListFragment).commit();
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            String unFormatedNumber = textView.getText().toString();
            unFormatedNumber.replace("-", "");
            unFormatedNumber.replace("(", "");
            unFormatedNumber.replace(")", "");
            if(unFormatedNumber.length() == 10) {
                NumberDatasource datasource = new NumberDatasource(getApplicationContext());
                datasource.open();
                    for(PhoneNumber otherNumber : datasource.getNumbers()) {
                        if(unFormatedNumber.equalsIgnoreCase(otherNumber.number)) {
                            return true;
                        }
                    }
                    PhoneNumber number = new PhoneNumber();
                    number.number = unFormatedNumber;
                datasource.create(number);
                datasource.close();
            }
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
            ft = getFragmentManager().beginTransaction();
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void numberClicked(String numberID) {
        AutoTextsListView autoTextsListView = AutoTextsListView.newInstance(numberID);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, autoTextsListView).addToBackStack(null).commit();
    }
}
