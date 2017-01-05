package com.asmedia.checkin_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    //RelativeLayout background;

    Button btn_scan, btn_admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_scan = (Button) findViewById(R.id.button_scan);
        btn_admin = (Button) findViewById(R.id.button_admin);

        btn_scan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //click button code here
                Intent i = new Intent(view.getContext(), ScanActivity.class);
                startActivity(i);
            }

        });

        btn_admin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //click button code here
                Intent i = new Intent(view.getContext(), LoginActivity.class);
                startActivity(i);

            }

        });

    }




}
