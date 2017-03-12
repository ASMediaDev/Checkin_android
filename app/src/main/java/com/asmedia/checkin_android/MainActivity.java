package com.asmedia.checkin_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {


    //RelativeLayout background;

    Button btn_scan, btn_admin;

    Realm realm;

    TextView main_db_status;

    public static final String TAG = "MainActivity";

    /**
     * Id to identify a camera permission request.
     */
    private static final int REQUEST_CAMERA = 0;

    /**
     * Id to identify a contacts permission request.
     */
    private static final int REQUEST_CONTACTS = 1;

    /**

     */
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};

    // Whether the Log Fragment is currently shown.
    private boolean mLogShown;

    /**
     * Root of the layout of this Activity.
     */
    private View mLayout;

    /**
     * Called when the 'show camera' button is clicked.
     * Callback is defined in resource layout definition.
     */
    public void showCamera(View view) {
        Log.i(TAG, "Show camera button pressed. Checking permission.");
        // BEGIN_INCLUDE(camera_permission)
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestCameraPermission();

        } else {

            // Camera permissions is already available, show the camera preview.
            Log.i(TAG,
                    "CAMERA permission has already been granted. Displaying camera preview.");

        }
        // END_INCLUDE(camera_permission)

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_db_status = (TextView) findViewById(R.id.main_db_status);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        updateDbStatus();

        btn_scan = (Button) findViewById(R.id.button_scan);
        btn_admin = (Button) findViewById(R.id.button_admin);

        btn_scan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Log.i(TAG, "Show camera button pressed. Checking permission.");
                // BEGIN_INCLUDE(camera_permission)
                // Check if the Camera permission is already available.
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has not been granted.

                    requestCameraPermission();

                } else {

                    // Camera permissions is already available, show the camera preview.
                    Log.i(TAG,
                            "CAMERA permission has already been granted. Displaying camera preview.");
                    Intent i = new Intent(view.getContext(), ScanActivityZXing.class);
                    startActivity(i);

                }
                // END_INCLUDE(camera_permission)
                //click button code here

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

    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(mLayout, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
        // END_INCLUDE(camera_permission_request)
    }


    public void updateDbStatus(){

        String loadedEvent;
        int attendeesCount;

        RealmResults<AttendeeObject> results = realm.where(AttendeeObject.class).findAll();

        if (results.isEmpty()){

            main_db_status.setText("Es befinden sich keine Gäste in der Datenbank!");

        }else {


            loadedEvent = results.get(0).getEventName();
            attendeesCount = results.size();
            Log.d("Event: ", loadedEvent);

            main_db_status.setText("Synchronisiertes Event:\n" + loadedEvent + "\n\n" + "Anzahl der Gäste: " + attendeesCount);

        }

    }




}
