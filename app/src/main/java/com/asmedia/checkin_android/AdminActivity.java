package com.asmedia.checkin_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Session session;

    Button btn_logout, btn_back;

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


}
