package com.asmedia.checkin_android;

/**
 * Created by alexanderseitz on 04.01.17.
 */

public class Events {

    private String eventname;
    private String eventid;

    public Events(String eventname, String eventid){

        this.setEventname(eventname);
        this.setEventid(eventid);

    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }
}
