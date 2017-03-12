package com.asmedia.checkin_android.Attendee_Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.asmedia.checkin_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderseitz on 04.01.17.
 */

public class AttendeesAdapter extends ArrayAdapter {

    List list = new ArrayList();


    public AttendeesAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(Attendees object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        AttendeesHolder attendeesHolder;

        if(row==null){

            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflater.inflate(R.layout.row_layout_attendees,parent,false);

            attendeesHolder = new AttendeesHolder();
            attendeesHolder.tx_firstname = (TextView)row.findViewById(R.id.tx_firstname);
            attendeesHolder.tx_lastname = (TextView)row.findViewById(R.id.tx_lastname);
            row.setTag(attendeesHolder);

        }else{
            attendeesHolder = (AttendeesHolder) row.getTag();

        }

        Attendees attendees = (Attendees) this.getItem(position);
        attendeesHolder.tx_firstname.setText(attendees.getFirst_name());
        attendeesHolder.tx_lastname.setText(attendees.getLast_name());

        return row;
    }

    static class AttendeesHolder{
        TextView tx_firstname, tx_lastname;

    }
}

