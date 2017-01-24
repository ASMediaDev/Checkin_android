package com.asmedia.checkin_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import io.realm.Realm;
import io.realm.RealmResults;

public class AdminActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Session session;

    Button btn_logout, btn_back;

    String json_string;

    private ArrayList<Events> eventsArrayList;

    private String URL_EVENTS = "http://api.ticketval.de/getEvents.php";
    private String URL_ATTENDEES = "http://api.ticketval.de/getAttendees.php?eventId=";


    String [] spinnerList = {};

    Spinner spinnerEvents;


    List<String> eventTitles = new ArrayList<String>();

    JSONArray jsonArray;

    Realm realm;

    TextView displayJson;

    int selectedEvent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Realm.init(this);
        realm = Realm.getDefaultInstance();


        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_back = (Button) findViewById(R.id.btn_back);

        btn_logout.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view){
                session = new Session(view.getContext());
                session.setLoggedIn(false);

                Intent i = new Intent(view.getContext(), LoginActivity.class);
                startActivity(i);

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        spinnerEvents = (Spinner) findViewById(R.id.spinner);

        eventsArrayList = new ArrayList<Events>();

        spinnerEvents.setOnItemSelectedListener(this);

        displayJson = (TextView) findViewById(R.id.textView_displayJSON);

        new GetEvents().execute();

        //new GetAttendees().execute(1);



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(this,"Selected: " + eventsArrayList.get(i).getEventname(),Toast.LENGTH_SHORT).show();
        selectedEvent = (i+1);
        //new GetAttendees().execute(i+1);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class GetEvents extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Fetching food categories..");
            pDialog.setCancelable(false);
            pDialog.show();
*/
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_EVENTS, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONArray jsonArr = new JSONArray(json);
                    if (jsonArr != null) {
                        JSONArray categories = jsonArr;


                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            Events cat = new Events(catObj.getString("title"),
                                    catObj.getString("id"));
                            eventsArrayList.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //if (pDialog.isShowing())
            //  pDialog.dismiss();
            populateSpinner();
        }

    }

    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();

        //txtCategory.setText("");

        for (int i = 0; i < eventsArrayList.size(); i++) {
            lables.add(eventsArrayList.get(i).getEventname());
            Log.d("Event:", String.valueOf(eventsArrayList.get(i)));
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerEvents.setAdapter(spinnerAdapter);
    }


    private class GetAttendees extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Fetching food categories..");
            pDialog.setCancelable(false);
            pDialog.show();
*/
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            ServiceHandler jsonParser = new ServiceHandler();
            String requestUrl = URL_ATTENDEES + integers[0];
            Log.d("URL",requestUrl);
            String json = jsonParser.makeServiceCall(requestUrl, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONArray jsonArr = new JSONArray(json);
                    if (jsonArr != null) {

                        Log.d("AdminActivity", "before intent");
                        Intent i = new Intent(getApplicationContext(), AttendeesListView.class);
                        i.putExtra("json_data", json);
                        startActivity(i);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //if (pDialog.isShowing())
            //  pDialog.dismiss();
            //populateSpinner();
        }

    }


    private class InsertAttendees extends AsyncTask<Object, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected String doInBackground(Object... integers) {
            ServiceHandler jsonParser = new ServiceHandler();
            String requestUrl = URL_ATTENDEES + integers[0];
            Log.d("URL",requestUrl);
            String json = jsonParser.makeServiceCall(requestUrl, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);



            return json;
        }



        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            //if (pDialog.isShowing())
            //  pDialog.dismiss();
            //populateSpinner();

            AlertDialog.Builder a_builder = new AlertDialog.Builder(AdminActivity.this);
            a_builder.setMessage("Bisherige Datensätze werden überschrieben! Fortfahren?")
                    .setCancelable(false)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        insertIntoRealm(result);


                        }
                    })
                    .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alert = a_builder.create();
            alert.setTitle("Achtung!");
            alert.show();

            
        }

    }

/*
session = new Session(this);
                session.setLoggedIn(false);
 */


    public void parseJSON(View view) throws ExecutionException, InterruptedException {

        new GetAttendees().execute(selectedEvent);


    }

    public void showDB(View view){

        TextView displayData = (TextView) findViewById(R.id.textView_displayDB);

        displayData.setText("");

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<AttendeeObject> results = realm.where(AttendeeObject.class).findAll();

        for(int i=0;i< results.size();i++){

            displayData.append(results.get(i).getFirstName() + results.get(i).getLastName() + " ");
        }

    }

    public void insertIntoRealm(String json){

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        if (json != null) {
            try {
                JSONArray jsonArr = new JSONArray(json);
                if (jsonArr != null) {

                    for (int i = 0; i < jsonArr.length(); i++) {
                        realm.beginTransaction();
                        AttendeeObject obj = realm.createObject(AttendeeObject.class);
                        JSONObject catObj = (JSONObject) jsonArr.get(i);

                        obj.setFirstName(catObj.getString("first_name"));
                        obj.setLastName(catObj.getString("last_name"));
                        obj.setOrderId(catObj.getInt("order_id"));
                        obj.setTicketId(catObj.getInt("ticket_id"));
                        obj.setPrivate_reference_number(catObj.getInt("private_reference_number"));
                        obj.setArrived(false);

                        Log.d("Insertion for ",obj.getFirstName() + obj.getLastName() +"complete");

                        realm.commitTransaction();


                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("JSON Data", "Didn't receive any data from server!");
        }



    }

    public void insertAttendees(View view){

        new InsertAttendees().execute(selectedEvent);

    }



}
