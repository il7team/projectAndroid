package com.bitm.alfa_travel_mate.model;

import java.io.Serializable;

/**
 * Created by Jibunnisa on 5/7/2017.
 */

public class EventExpence implements Serializable{

    String eventId;
    String expenceId;
    String details;
    String amount;
    String  date;
    String  time;

    public EventExpence() {
    }

    public EventExpence(String expenceId,String eventId, String details, String amount, String date, String time) {
        this.eventId = eventId;
        this.details = details;
        this.amount = amount;
        this.date=date;
        this.time=time;
        this.expenceId = expenceId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getExpenceId() {
        return expenceId;
    }

    public void setExpenceId(String expenceId) {
        this.expenceId = expenceId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
