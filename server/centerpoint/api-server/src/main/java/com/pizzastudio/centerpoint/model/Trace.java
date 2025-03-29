package com.pizzastudio.centerpoint.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Trace {
    @JsonProperty("no")
    public int no;
    @JsonProperty("lat")
    public double lat;
    @JsonProperty("lng")
    public double lng;
    @JsonProperty("minute")
    public int minute;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Trace(int no, double lat, double lng, int minute) {
        this.no = no;
        this.lat = lat;
        this.lng = lng;
        this.minute = minute;
    }


}
