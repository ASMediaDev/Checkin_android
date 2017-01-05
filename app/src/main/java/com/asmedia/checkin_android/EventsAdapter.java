package com.asmedia.checkin_android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderseitz on 04.01.17.
 */

public class EventsAdapter extends ArrayAdapter {

    List list = new ArrayList();


    public EventsAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(Events object) {
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
        EventHolder eventHolder;

        if(row==null){

            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflater.inflate(R.layout.row_layout,parent,false);

            eventHolder = new EventHolder();
            eventHolder.tx_eventname = (TextView)row.findViewById(R.id.tx_eventname);
            eventHolder.tx_eventid = (TextView)row.findViewById(R.id.tx_eventid);
            row.setTag(eventHolder);

        }else{
            eventHolder = (EventHolder)row.getTag();

        }

        Events events = (Events)this.getItem(position);
        eventHolder.tx_eventname.setText(events.getEventname());
        eventHolder.tx_eventid.setText(events.getEventid());

        return row;
    }

    static class EventHolder{
        TextView tx_eventname, tx_eventid;

    }
}

