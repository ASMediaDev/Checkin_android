package com.asmedia.checkin_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivityZXing extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_zxing);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();


    }

    @Override
    protected void onPause(){
        super.onPause();
        mScannerView.stopCamera();

    }

    @Override
    public void handleResult(Result result) {

        Log.d("SCAN: ", result.getText());

        int private_reference_number = 0;
        try {
            private_reference_number = Integer.parseInt(result.getText());
        } catch (NumberFormatException nfe) {
            Log.d("Parse: ", "Could not parse " + nfe);
        }

        if (ticketExists(private_reference_number)) {

            AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
            a_builder.setMessage("Ticket gültig!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            mScannerView.resumeCameraPreview(ScanActivityZXing.this);


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

        }else{

            AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
            a_builder.setMessage("Ticketcode ungültig!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            mScannerView.resumeCameraPreview(ScanActivityZXing.this);


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
