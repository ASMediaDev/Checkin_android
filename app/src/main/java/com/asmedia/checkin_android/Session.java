package com.asmedia.checkin_android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alexanderseitz on 03.01.17.
 */

public class Session {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;


    public Session(Context ctx){
        this.ctx = ctx;

        prefs = ctx.getSharedPreferences("checkin", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedIn(boolean loggedin){

        editor.putBoolean("loggedInMode", loggedin);
        editor.commit();

    }

    public boolean loggedin(){

        return prefs.getBoolean("loggedInMode", false);

    }





}
