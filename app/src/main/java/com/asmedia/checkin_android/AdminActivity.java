package com.asmedia.checkin_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AdminActivity extends AppCompatActivity {

    private Session session;

    Button btn_logout, btn_back;

    String json_string;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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
    }

/*
session = new Session(this);
                session.setLoggedIn(false);
 */


    public void getJSON(View view){

    new BackgroundTask().execute();


    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String JSON_STRING;
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://api.ticketval.de/getEvents.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while((JSON_STRING = bufferedReader.readLine())!= null){
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            TextView textView = (TextView)findViewById(R.id.textView_displayJSON);
            textView.setText(result);
            json_string = result;
        }
    }

    public void parseJSON(View view){

        if(json_string==null){

            Toast.makeText(getApplicationContext(),"First get JSON!",Toast.LENGTH_LONG).show();

        } else {


            Intent i = new Intent(this, DisplayListView.class);
            i.putExtra("json_data",json_string);
            startActivity(i);

        }

    }

}
