package com.bitm.alfa_travel_mate.model;

/**
 * Created by Shamon on 5/2/2017.
 */

public class TravelMoment {

    private String momentId;
    private String eventId;
    private String description;
    private String imageUrl;
    String  date;
    String  time;

    public TravelMoment(String momentId,String eventId,String description, String imageUrl, String date, String time) {

        this.momentId = momentId;
        this.eventId = eventId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.time = time;

    }

    public TravelMoment() {


    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String eventId) {
        this.momentId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
