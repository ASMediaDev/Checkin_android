package com.asmedia.checkin_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AdminActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Session session;

    Button btn_logout;

    ImageButton btn_back;

    String json_string;

    private ArrayList<Events> eventsArrayList;

    private String URL_EVENTS = "https://ticketval.de/api/getEvents";
    private String URL_ATTENDEES = "https://ticketval.de/api/getAttendees?eventId=";


    String [] spinnerList = {};

    Spinner spinnerEvents;


    List<String> eventTitles = new ArrayList<String>();

    JSONArray jsonArray;

    Realm realm;

    View statusBar;

    TextView statusBarTextView;

    int selectedEvent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        statusBarTextView = (TextView)findViewById(R.id.db_status_textView);
        statusBar = findViewById(R.id.status_bar_bg);

        updateStatusBar();

        Realm.init(this);
        realm = Realm.getDefaultInstance();



        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_back = (ImageButton) findViewById(R.id.btn_back);

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


        getEvents();



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        selectedEvent = (i+1);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //API Methods

    public void getEvents(){

        String url = "https://ticketval.de/api/getEvents";

        String accessToken;

        SharedPreferences sharedPref = getSharedPreferences("accessTokens", Context.MODE_PRIVATE);

        accessToken = sharedPref.getString("accessToken", "");

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.addHeader("Authorization", "Bearer " + accessToken);

        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess (int statusCode, Header[] headers, JSONArray response){

                Log.d("Response: ", String.valueOf(response));

                if (response != null) {
                    try {
                        //JSONArray jsonArr = new JSONArray(response);
                        if (response != null) {
                            JSONArray categories = response;


                            for (int i = 0; i < categories.length(); i++) {
                                JSONObject catObj = (JSONObject) categories.get(i);
                                Events cat = new Events(catObj.getString("title"),
                                        catObj.getString("id"));
                                eventsArrayList.add(cat);

                                populateSpinner();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("JSON Data", "Didn't receive any data from server!");
                }

            }

            @Override
            public void onFailure (int statusCode, Header[] headers, String responseString, Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", String.valueOf(statusCode));
                Log.d("Error getEvents: ", String.valueOf(throwable));
            }
        });
    }

    public void getAttendees(int eventId){

        String url = "https://ticketval.de/api/getAttendees/" + eventId;

        String accessToken;

        SharedPreferences sharedPref = getSharedPreferences("accessTokens", Context.MODE_PRIVATE);

        accessToken = sharedPref.getString("accessToken", "");

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.addHeader("Authorization", "Bearer " + accessToken);

        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess (int statusCode, Header[] headers, JSONArray response){

                Log.d("Response: ", String.valueOf(response));

                if (response != null) {

                    if (response != null) {

                        Log.d("AdminActivity", "before intent");
                        Intent i = new Intent(getApplicationContext(), AttendeesListView.class);
                        i.putExtra("json_data", String.valueOf(response));
                        startActivity(i);

                    }

                } else {
                    Log.e("JSON Data", "Didn't receive any data from server!");
                }

            }

            @Override
            public void onFailure (int statusCode, Header[] headers, String responseString, Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", String.valueOf(statusCode));
                Log.d("Error: ", String.valueOf(throwable));
            }
        });
    }


    public void insertAttendees(View view){

        String url = "https://ticketval.de/api/getAttendees/" + selectedEvent;

        String accessToken;

        SharedPreferences sharedPref = getSharedPreferences("accessTokens", Context.MODE_PRIVATE);

        accessToken = sharedPref.getString("accessToken", "");

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.addHeader("Authorization", "Bearer " + accessToken);

        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess (int statusCode, Header[] headers, final JSONArray response){

                Log.d("Response: ", String.valueOf(response));

                if (response != null) {

                    if (response != null) {

                        AlertDialog.Builder a_builder = new AlertDialog.Builder(AdminActivity.this);
                        a_builder.setMessage("Bisherige Datensätze werden überschrieben! Fortfahren?")
                                .setCancelable(false)
                                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        insertIntoRealm(String.valueOf(response));


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

                } else {
                    Log.e("JSON Data", "Didn't receive any data from server!");
                }

            }

            @Override
            public void onFailure (int statusCode, Header[] headers, String responseString, Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", String.valueOf(statusCode));
                Log.d("Error: ", String.valueOf(throwable));
            }
        });




    }

    public int countAttendees(){

        int attendeesCount = 0;

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<AttendeeObject> query = realm.where(AttendeeObject.class);

        RealmResults<AttendeeObject> results = query.findAll();

        if(results.isEmpty()){

            attendeesCount = 0;

        } else{

            attendeesCount = results.size();

        }


        return attendeesCount;
    }

    public void truncate_db(View view) {

        AlertDialog.Builder a_builder = new AlertDialog.Builder(AdminActivity.this);
        a_builder.setMessage("Alle Datensätze werden gelöscht! Fortfahren?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        realm.beginTransaction();
                        realm.deleteAll();
                        realm.commitTransaction();

                        updateStatusBar();
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



    public void parseJSON(View view) throws ExecutionException, InterruptedException {

        //new GetAttendees().execute(selectedEvent);
        getAttendees(selectedEvent);


    }


    public void insertIntoRealm(String json){

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        if (json != null) {
            try {
                JSONArray jsonArr = new JSONArray(json);
                if (jsonArr != null) {

                    int insertcounter = 0;

                    for (int i = 0; i < jsonArr.length(); i++) {
                        realm.beginTransaction();

                        JSONObject catObj = (JSONObject) jsonArr.get(i);

                        Log.d("Cancelled", (String) catObj.get("is_cancelled"));

                        if(catObj.getInt("is_cancelled")==1){

                            Log.d("Cancelled"," true");
                            realm.cancelTransaction();

                        }else {

                            AttendeeObject obj = realm.createObject(AttendeeObject.class, catObj.getInt("private_reference_number"));
                            obj.setFirstName(catObj.getString("first_name"));
                            obj.setLastName(catObj.getString("last_name"));
                            obj.setOrderId(catObj.getInt("order_id"));
                            obj.setTicketId(catObj.getInt("ticket_id"));
                            //obj.setPrivate_reference_number(catObj.getInt("private_reference_number"));
                            obj.setArrived(false);
                            obj.setEventName(String.valueOf(eventsArrayList.get(catObj.getInt("event_id") - 1).getEventname()));
                            obj.setIs_cancelled(false);

                            Log.d("Insertion for ", obj.getFirstName() + obj.getLastName() + "complete" + "Event: " +
                                    String.valueOf(eventsArrayList.get(catObj.getInt("event_id") - 1).getEventname()));

                            realm.commitTransaction();
                            insertcounter++;
                        }
                    }
                    updateStatusBar();

                    AlertDialog.Builder a_builder = new AlertDialog.Builder(AdminActivity.this);
                    a_builder.setMessage("Es wurden " + insertcounter + " Datensätze importiert")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Synchronisierung abgeschlossen!");
                    alert.show();


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("JSON Data", "Didn't receive any data from server!");
        }



    }

    public void updateStatusBar(){

        if(countAttendees()==0){
            statusBar.setBackgroundColor(Color.RED);
            statusBarTextView.setText("Anzahl der Gäste in der Datenbank:\n " + countAttendees());
        }else{
            statusBar.setBackgroundColor(Color.GREEN);
            statusBarTextView.setText("Anzahl der Gäste in der Datenbank:\n " + countAttendees());
        }
    }


    public void checkin_redirect(View view) {

        Intent i = new Intent(view.getContext(), ScanActivityZXing.class);
        startActivity(i);
    }
}
