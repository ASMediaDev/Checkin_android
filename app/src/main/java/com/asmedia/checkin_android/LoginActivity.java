package com.asmedia.checkin_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText UsernameEt, PasswordEt;

    //Session session;

    private Session session;

    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UsernameEt = (EditText)findViewById(R.id.et_username);
        PasswordEt = (EditText)findViewById(R.id.et_password);

        session = new Session(this);




        if(session.loggedin()){

            Log.d(TAG, "logged in");

            Intent i = new Intent(this, AdminActivity.class);
            startActivity(i);

        } else{

            Log.d(TAG, "not logged in");
        }

    }

    public void onLogin(View view){

        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";

        String status;

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);

        backgroundWorker.execute(type, username, password);




    }

    public void onLogout(){

        session = new Session(this);
        session.setLoggedIn(false);

    }

    public void redirect(View view){


        Intent i = new Intent(view.getContext(), AdminActivity.class);
        startActivity(i);

    }
}
