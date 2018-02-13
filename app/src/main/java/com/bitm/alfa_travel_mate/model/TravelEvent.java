package com.bitm.alfa_travel_mate.model;

/**
 * Created by Shamon on 5/2/2017.
 */

public class TravelEvent {
    private String userId;
    private String eventId;
    private String travelDestination;
    private String estimateBudget;
    private String fromDate;
    private String toDate;
   /* private String accessKey;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }*/

    public TravelEvent(String eventId,String userId, String travelDestination, String estimateBudget, String fromDate, String toDate) {
        this.userId = userId;
        this.travelDestination = travelDestination;
        this.estimateBudget = estimateBudget;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.eventId = eventId;
    }

    public TravelEvent() {


    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTravelDestination() {
        return travelDestination;
    }

    public String getEstimateBudget() {
        return estimateBudget;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }
}
