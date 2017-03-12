package com.asmedia.checkin_android.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.asmedia.checkin_android.R;
import com.asmedia.checkin_android.Session;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText UsernameEt, PasswordEt;

    //Session session;

    private Session session;

    private static final String TAG = "LoginActivity";

    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar)findViewById(R.id.loginprogress);
        progressBar.setVisibility(View.GONE);

        UsernameEt = (EditText)findViewById(R.id.et_username);
        PasswordEt = (EditText)findViewById(R.id.et_password);


        session = new Session(this);

        if(session.loggedin()){

            Log.d(TAG, "logged in");

            String username = String.valueOf(UsernameEt.getText());
            String password = String.valueOf(PasswordEt.getText());


            //Intent i = new Intent(this, AdminActivity.class);
            //startActivity(i);

            validateAccessToken(username, password, progressBar);

        } else{

            Log.d(TAG, "not logged in");
        }


    }

    public void onLogin(final View view) throws JSONException{

        final String username = UsernameEt.getText().toString();
        final String password = PasswordEt.getText().toString();

        if(username.isEmpty() || password.isEmpty()){

            AlertDialog.Builder a_builder = new AlertDialog.Builder(LoginActivity.this);
            a_builder.setMessage("Bitte beide Felder ausfüllen!")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });

            AlertDialog alert = a_builder.create();
            alert.setTitle("Achtung!");
            alert.show();


        }else {

            progressBar.setVisibility(View.VISIBLE);

            String url = "https://ticketval.de/api/login";

            RequestParams params = new RequestParams();
            params.put("userName", username);
            params.put("userPassword", password);

            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

            client.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.d("Response: ", String.valueOf(response));

                    try {
                        int responsecode = response.getInt("status");
                        Log.d("Status: ", String.valueOf(responsecode));

                        if (responsecode == 200) {
                            Log.d(TAG, "success");
                            //alertDialog.setMessage("SUCCESS");
                            //alertDialog.show();


                       SharedPreferences sharedPref = getSharedPreferences("userCredentials", Context.MODE_PRIVATE);

                       SharedPreferences.Editor editor = sharedPref.edit();
                       editor.putString("username", username);
                       editor.putString("userpassword", password);
                       editor.apply();

                       session.setLoggedIn(true);

                            //getAccessToken(username,password);
                            Log.d(TAG, "jump into validation");
                            validateAccessToken(username, password, progressBar);
                            //redirect(view);


                        } else if (responsecode == 403) {
                            AlertDialog.Builder a_builder = new AlertDialog.Builder(LoginActivity.this);
                            a_builder.setMessage("Zugangsdaten nicht korrekt!")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            progressBar.setVisibility(View.GONE);
                                            return;
                                        }
                                    });

                            AlertDialog alert = a_builder.create();
                            alert.setTitle("Achtung!");
                            alert.show();
                        } else if (responsecode == 444){
                            AlertDialog.Builder a_builder = new AlertDialog.Builder(LoginActivity.this);
                            a_builder.setMessage("Benutzer nicht gefunden!")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            progressBar.setVisibility(View.GONE);
                                            return;
                                        }
                                    });

                            AlertDialog alert = a_builder.create();
                            alert.setTitle("Achtung!");
                            alert.show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("Failed: ", String.valueOf(statusCode));
                    Log.d("Error: ", String.valueOf(throwable));

                    AlertDialog.Builder a_builder = new AlertDialog.Builder(LoginActivity.this);
                    a_builder.setMessage("Bitte Internetverbindung prüfen!")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    progressBar.setVisibility(View.GONE);
                                    return;
                                }
                            });

                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Achtung!");
                    alert.show();
                }


            });
        }


    }


    public void redirect(View view){


        Log.d("Redirect: ", "redirecting....");

        Intent i = new Intent(view.getContext(), AdminActivity.class);
        startActivity(i);

    }

    public void getAccessToken(String username, String password, final ProgressBar progressBar){

        String url = "https://ticketval.de/oauth/token";

        RequestParams params = new RequestParams();
        params.put("grant_type", "password");
        params.put("client_id", "11");
        params.put("client_secret", "e0cJTi4FT8oqBITsqakgsFYdhkD4CtrqVmqrtVQJ");
        params.put("username", username);
        params.put("password", password);

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        client.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess (int statusCode, Header[] headers, JSONObject response){

                Log.d("Response: ", String.valueOf(response));
                try {
                    String accessToken = response.getString("access_token");

                    SharedPreferences sharedPref = getSharedPreferences("accessTokens", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("accessToken", accessToken);
                    editor.apply();

                    Log.d("AccessToken: ", accessToken);

                    progressBar.setVisibility(View.GONE);

                    redirect(findViewById(android.R.id.content));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure (int statusCode, Header[] headers, String responseString, Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", String.valueOf(statusCode));
                Log.d("Error: ", String.valueOf(throwable));
                progressBar.setVisibility(View.GONE);
            }


        });


    }

    public void validateAccessToken(final String username, final String password, final ProgressBar progressBar){


        String url = "https://ticketval.de/api/validateToken";

        String accessToken;


        SharedPreferences sharedPref = getSharedPreferences("accessTokens", Context.MODE_PRIVATE);

        accessToken = sharedPref.getString("accessToken", "");

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.addHeader("Authorization", "Bearer " + accessToken);

        Log.d("Token: ", accessToken);

        Log.d(TAG, "validating token request");

        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess (int statusCode, Header[] headers, JSONObject response){

                Log.d("Response: ", String.valueOf(response));

                if (response != null) {
                    try {
                        //JSONArray jsonArr = new JSONArray(response);
                        if (response != null) {

                            String debug = response.getString("status");
                            Log.d("Response: ", debug);

                            if (response.getInt("status") == 200){

                                progressBar.setVisibility(View.GONE);

                                redirect(findViewById(android.R.id.content));

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
                Log.d(TAG,"token not valid, starting getAccessToken process");
                getAccessToken(username, password, progressBar);

            }
        });




    }

}
