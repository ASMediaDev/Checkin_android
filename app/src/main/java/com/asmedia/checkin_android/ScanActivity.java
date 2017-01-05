package com.asmedia.checkin_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class ScanActivity extends AppCompatActivity {

    Button btn_back;
    TextView showcode;

    private SurfaceView mySurfaceView;
    private QREader qrEader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        //Setup SurfaceView
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_view);

        //Init QReader

        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {

                Log.d("QREader", "Value : "+ data);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    updateLabel(data);

                    }
                });


                qrEader.releaseAndCleanup();

            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();


        btn_back = (Button) findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);

            }


        });


    }

    @Override
    public void onResume(){

        super.onResume();
        qrEader.initAndStart(mySurfaceView);
    }

    @Override
    public void onPause(){

        super.onPause();
        qrEader.releaseAndCleanup();

    }

    public void updateLabel(String content){

        showcode = (TextView) findViewById(R.id.textView2);

        showcode.setText(content);

    }

}
