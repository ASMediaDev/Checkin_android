package com.asmedia.checkin_android;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by alexanderseitz on 21.01.17.
 */

public class AttendeeObject extends RealmObject {

    int ticketId;
    int private_reference_number;
    int orderId;
    String lastName;
    String firstName;
    Date checkinTime;
    Boolean arrived;

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getPrivate_reference_number() {
        return private_reference_number;
    }

    public void setPrivate_reference_number(int private_reference_number) {
        this.private_reference_number = private_reference_number;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(Date checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Boolean getArrived() {
        return arrived;
    }

    public void setArrived(Boolean arrived) {
        this.arrived = arrived;
    }
}
