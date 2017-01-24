package com.asmedia.checkin_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ScanActivity extends AppCompatActivity {

    Button btn_back;
    TextView showcode;

    private SurfaceView mySurfaceView;
    private QREader qrEader;

    Realm realm;



    int private_reference_number = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        //showAlert();

        //Setup SurfaceView
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_view);

        //Init QReader

        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {


            @Override
            public void onDetected(final String data) {

                Log.d("QREader", "Value : "+ data);

                try {
                    private_reference_number = Integer.parseInt(data);
                }
                catch(NumberFormatException nfe){
                    Log.d("Parse: ","Could not parse " + nfe);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        updateLabel(data);
                        showAlert();

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

    public void showAlert(){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        a_builder.setMessage("Alert Test")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //qrEader.releaseAndCleanup();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                     dialogInterface.cancel();
                    }
                });

        AlertDialog alert = a_builder.create();
        alert.setTitle("Alert!");
        alert.show();
    }



    public boolean ticketExists(int private_reference_number){


        RealmQuery<AttendeeObject> query = realm.where(AttendeeObject.class);
        query.equalTo("private_reference_number",private_reference_number);

        RealmResults<AttendeeObject> results = query.findAll();

        if(results.isEmpty()){

            Log.d("DBController","Ticket does not exist!");
            return false;

        } else{

            Log.d("DBController","Ticket exists!");
            return true;

        }




    }

}
