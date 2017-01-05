package com.asmedia.checkin_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayListView extends AppCompatActivity {

    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listView;
    EventsAdapter eventsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list_view);
        listView = (ListView) findViewById(R.id.listview);
        eventsAdapter = new EventsAdapter(this, R.layout.row_layout);

        listView.setAdapter(eventsAdapter);



        json_string = getIntent().getExtras().getString("json_data");

        if (json_string == null) {

            Log.d("Error","Empty Json");

        } else {

            try {
                jsonArray = new JSONArray(json_string);


                String eventname, eventid;
                int count = 0;

                while (count < jsonArray.length()) {

                    JSONObject JO = jsonArray.getJSONObject(count);
                    Log.d("JSON", String.valueOf(JO));
                    eventname = JO.getString("title");
                    eventid = JO.getString("id");

                    Events events = new Events(eventname, eventid);

                    eventsAdapter.add(events);

                    count++;


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
