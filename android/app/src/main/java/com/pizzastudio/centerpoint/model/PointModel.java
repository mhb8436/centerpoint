package com.pizzastudio.centerpoint.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PointModel {

    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;
    @SerializedName("tr")
    private List<TraceModel> tr;

    public PointModel() {
    }

    public PointModel(String name, String type, Double lat, Double lng) {
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }

    public PointModel(String name, String type, Double lat, Double lng, List<TraceModel> tr) {
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.tr = tr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<TraceModel> getTr() {
        return tr;
    }

    public void setTr(List<TraceModel> tr) {
        this.tr = tr;
    }
}
