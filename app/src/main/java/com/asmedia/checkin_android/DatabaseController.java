package com.asmedia.checkin_android;

/**
 * Created by alexanderseitz on 24.01.17.
 */

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class DatabaseController extends Application {

    Realm realm;

    public DatabaseController(){

        Realm.init(this);
        realm = Realm.getDefaultInstance();


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
