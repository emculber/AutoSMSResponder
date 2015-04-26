package com.ultimatumedia.autosmsresponder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ultimatumedia.autosmsresponder.DataInput.NewAutoMessage;
import com.ultimatumedia.autosmsresponder.R;
import com.ultimatumedia.autosmsresponder.SMS.PhoneNumber;

import java.util.ArrayList;

/**
 * Created by Erik on 4/25/15.
 */
public class NewAutoMessageSpinnerBaseAdapter extends BaseAdapter {

    String[] list;
    Context context;

    public NewAutoMessageSpinnerBaseAdapter(Context context) {
        this.context = context;
        list = context.getResources().getStringArray(R.array.new_auto_message_conditions_array);
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SpinnerHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.new_auto_message_spinner_item, null);
            viewHolder = new SpinnerHolder();
            viewHolder.spinnerItem = (TextView) view.findViewById(R.id.SpinnerItem);

            view.setTag(viewHolder);
        } else {
            viewHolder = (SpinnerHolder) view.getTag();
        }
        viewHolder.spinnerItem.setText(list[position]);
        return view;
    }

    private class SpinnerHolder {
        public TextView spinnerItem;
    }
}
