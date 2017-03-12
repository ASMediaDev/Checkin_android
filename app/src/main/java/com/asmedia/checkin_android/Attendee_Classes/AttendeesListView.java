package com.asmedia.checkin_android.Attendee_Classes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.asmedia.checkin_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AttendeesListView extends AppCompatActivity {

    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listView;
    AttendeesAdapter attendeesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees_list_view);
        listView = (ListView) findViewById(R.id.attendeesListView);
        attendeesAdapter = new AttendeesAdapter(this, R.layout.row_layout_attendees);

        listView.setAdapter(attendeesAdapter);



        json_string = getIntent().getExtras().getString("json_data");

        if (json_string == null) {

            Log.d("Error","Empty Json");

        } else {

            try {
                jsonArray = new JSONArray(json_string);


                String first_name, last_name;
                int count = 0;

                while (count < jsonArray.length()) {

                    JSONObject JO = jsonArray.getJSONObject(count);
                    Log.d("JSON", String.valueOf(JO));
                    first_name = JO.getString("first_name");
                    last_name = JO.getString("last_name");

                    Log.d("ALV",first_name);
                    Log.d("ALV",last_name);

                    Attendees attendees = new Attendees(first_name, last_name);

                    attendeesAdapter.add(attendees);
                   //Log.d("ALV", String.valueOf(attendeesAdapter);

                    count++;


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
