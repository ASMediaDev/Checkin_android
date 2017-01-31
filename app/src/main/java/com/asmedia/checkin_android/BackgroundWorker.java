package com.asmedia.checkin_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by alexanderseitz on 03.01.17.
 */

public class BackgroundWorker extends AsyncTask<String,Void,String> {

    Context context;

    AlertDialog alertDialog;

    private static final String TAG = "BackgroundWorker";

    private Session session;


    BackgroundWorker (Context ctx) {

        context = ctx;
    }


    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String login_url = "http://laravel.ticketval.de/api/login";

        if(type.equals("login")){

            try {
                String user_name = params[1];
                String password = params[2];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("userName","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+URLEncoder.encode("userPassword","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";

                while((line = bufferedReader.readLine()) != null){
                    result += line;

                }

                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }



        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");

    }

    @Override
    protected void onPostExecute(String result) {

        //session = new Session(context);


        if (result.contains("200")){
            Log.d(TAG, "success");
            //alertDialog.setMessage("SUCCESS");
            //alertDialog.show();
            session.setLoggedIn(true);
            Intent i = new Intent(context, AdminActivity.class);
            context.startActivity(i);



        } else if(result.contains("403")){
            Log.d(TAG, "error 403");
            alertDialog.setMessage("ERROR 403");
            alertDialog.show();
            session.setLoggedIn(false);
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
