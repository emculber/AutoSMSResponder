package com.ultimatumedia.autosmsresponder;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.ultimatumedia.autosmsresponder.Displays.NumberListFragment;
import com.ultimatumedia.autosmsresponder.ResponseTree.ResponseTree;


public class MainActivity extends ActionBarActivity {

    private static final String[] COUNTRIES = new String[] { "Belgium",
            "France", "France_", "Italy", "Germany", "Spain" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView) v
                .findViewById(R.id.editText1);
        textView.setAdapter(adapter);


        ResponseTree responseTree = new ResponseTree();
        NumberListFragment numberListFragment = new NumberListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
