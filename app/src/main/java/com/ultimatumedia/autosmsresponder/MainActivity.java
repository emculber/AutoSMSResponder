package com.ultimatumedia.autosmsresponder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.ultimatumedia.autosmsresponder.DataInput.NewAutoMessage;
import com.ultimatumedia.autosmsresponder.Database.NumberDatasource;
import com.ultimatumedia.autosmsresponder.Displays.AutoTextsListView;
import com.ultimatumedia.autosmsresponder.Displays.NumberListFragment;
import com.ultimatumedia.autosmsresponder.ResponseTree.ResponseTree;
import com.ultimatumedia.autosmsresponder.SMS.PhoneNumber;
import com.ultimatumedia.autosmsresponder.Search.ClearableAutoCompleteTextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static MainActivity mainActivity;
    private ArrayList<String> Contancts;
    private ClearableAutoCompleteTextView searchBox;
    private NumberListFragment numberListFragment;
    private FragmentTransaction ft;
    private boolean searchOpen = false;
    private int backStackTrack = 0;
    private String lastNumebrId = "";
    private NewAutoMessage newAutoMessage;

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

        ActionBar actionBar = getSupportActionBar(); // you can use ABS or the non-bc ActionBar

// what's mainly important here is DISPLAY_SHOW_CUSTOM. the rest is optional
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);


        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate the view that we created before
        View v = inflater.inflate(R.layout.actionbar, null);
        // the view that contains the search "magnifier" icon
        // the view that contains the new clearable autocomplete text view
        searchBox =  (ClearableAutoCompleteTextView) v.findViewById(R.id.search_box);

        // start with the text view hidden in the action bar
        searchBox.setVisibility(View.INVISIBLE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Contancts);
        searchBox.setAdapter(arrayAdapter);

        searchBox.setOnClearListener(new ClearableAutoCompleteTextView.OnClearListener() {

            @Override
            public void onClear() {
                toggleSearch(true);
            }
        });

        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)parent.getItemAtPosition(position);
                String[] split = value.split("--");
                addNumber(split[0], split[1]);
                toggleSearch(true);
            }

        });

        actionBar.setCustomView(v);

        toggleSearch(true);
        numberListView();
        //newAutoMessage();
    }



    protected void toggleSearch(boolean reset) {
        ClearableAutoCompleteTextView searchBox = (ClearableAutoCompleteTextView) findViewById(R.id.search_box);
        if (reset) {
            // hide search box and show search icon
            searchBox.setText("");
            searchBox.setVisibility(View.GONE);
            searchOpen = false;
            // hide the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
        } else {
            // hide search icon and show search box
            searchBox.setVisibility(View.VISIBLE);
            searchBox.requestFocus();
            searchOpen = true;
            // show the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
        }

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
            if(backStackTrack == 0) {
                toggleSearch(false);
                if(searchOpen) {
                    addNumber("", searchBox.getText().toString());
                }
            }
            if(backStackTrack == 1) {
                newAutoMessage();
            }
            if(backStackTrack == 2) {
                newAutoMessage.addNewMessage();
                numberClicked(lastNumebrId);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNumber(String name, String pNumber) {
        String unFormatedNumber = pNumber;
        unFormatedNumber.replace("-", "");
        unFormatedNumber.replace("(", "");
        unFormatedNumber.replace(")", "");
        if(unFormatedNumber.length() >= 7) {
            NumberDatasource datasource = new NumberDatasource(getApplicationContext());
            datasource.open();
            for(PhoneNumber otherNumber : datasource.getNumbers()) {
                if(unFormatedNumber.equalsIgnoreCase(otherNumber.number)) {
                    return;
                }
            }
            PhoneNumber number = new PhoneNumber();
            number.name = name;
            number.number = unFormatedNumber;
            datasource.create(number);
            datasource.close();
        }
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        ft = getFragmentManager().beginTransaction();
        ft.detach(currentFragment);
        ft.attach(currentFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void numberListView() {
        numberListFragment = new NumberListFragment();
        ft = getFragmentManager().beginTransaction();
        if(backStackTrack != 1) {
            ft.add(R.id.fragment_container, numberListFragment).commit();
        }else {
            ft.replace(R.id.fragment_container, numberListFragment).commit();
        }
        backStackTrack = 0;
    }

    public void numberClicked(String numberID) {
        lastNumebrId = numberID;
        AutoTextsListView autoTextsListView = AutoTextsListView.newInstance(numberID);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, autoTextsListView);
        ft.commit();
        backStackTrack = 1;
    }

    public void newAutoMessage() {
        newAutoMessage = new NewAutoMessage();
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, newAutoMessage);
        ft.commit();
        backStackTrack = 2;
    }

    @Override
    public void onBackPressed() {
        if(backStackTrack == 1) {
            numberListView();
        } else {
            super.onBackPressed();
        }
    }
}
