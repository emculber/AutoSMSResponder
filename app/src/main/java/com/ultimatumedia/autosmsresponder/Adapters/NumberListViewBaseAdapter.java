package com.ultimatumedia.autosmsresponder.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ultimatumedia.autosmsresponder.Database.NumberDatasource;
import com.ultimatumedia.autosmsresponder.R;
import com.ultimatumedia.autosmsresponder.SMS.PhoneNumber;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by erik on 4/24/15.
 */
public class NumberListViewBaseAdapter extends BaseAdapter {

    private NumberDatasource datasource;
    private Context context;

    public NumberListViewBaseAdapter(Context context) {
        datasource = new NumberDatasource(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        datasource.open();
        int size = datasource.getNumbers().size();
        datasource.close();
        return size;
    }

    @Override
    public Object getItem(int position) {
        datasource.open();
        ArrayList<PhoneNumber> numbers = datasource.getNumbers();
        datasource.close();
        return numbers.get(position);
    }

    @Override
    public long getItemId(int position) {
        datasource.open();
        ArrayList<PhoneNumber> numbers = datasource.getNumbers();
        datasource.close();
        return numbers.get(position).numberId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        phoneNumberHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.number_listview_item, null);
            viewHolder = new phoneNumberHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.numberListViewItemName);
            viewHolder.number = (TextView) view.findViewById(R.id.numberListViewItemPhonenumber);
            viewHolder.active = (CheckBox) view.findViewById(R.id.numberListViewActive);



            view.setTag(viewHolder);
        } else {
            viewHolder = (phoneNumberHolder) view.getTag();
        }
        final PhoneNumber phoneNumber = (PhoneNumber)getItem(position);
        if(!phoneNumber.name.equals(""))
            viewHolder.name.setText(phoneNumber.name);
        viewHolder.number.setText(phoneNumber.number);
        viewHolder.active.setChecked(phoneNumber.active.contains("T"));
        viewHolder.active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasource.open();
                if(phoneNumber.active .contains("F"))
                    phoneNumber.active = "T";
                else
                    phoneNumber.active = "F";
                datasource.updateActive(phoneNumber);
                datasource.close();
            }
        });
        return view;
    }

    private class phoneNumberHolder {
        public TextView name;
        public TextView number;
        public CheckBox active;
    }
}
