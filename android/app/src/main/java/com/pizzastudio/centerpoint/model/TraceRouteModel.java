package com.pizzastudio.centerpoint.model;

import com.google.gson.annotations.SerializedName;

public class TraceRouteModel {
    @SerializedName("no")
    private Integer no;
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;
    @SerializedName("minute")
    private Long minute;

    public TraceRouteModel(){

    }
    public TraceRouteModel(Integer no, Double lat, Double lng, Long minute) {
        this.no = no;
        this.lat = lat;
        this.lng = lng;
        this.minute = minute;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Long getMinute() {
        return minute;
    }

    public void setMinute(Long minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "TraceRouteModel{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
