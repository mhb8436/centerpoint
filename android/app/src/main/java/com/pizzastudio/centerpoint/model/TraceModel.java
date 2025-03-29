package com.pizzastudio.centerpoint.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TraceModel {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;
    @SerializedName("recommend_place")
    private List<RecommendPlaceModel> recommend_place;
    @SerializedName("trace_list")
    private List<TraceRouteModel> trace_list;
    public TraceModel(){

    }

    public TraceModel(int id, String name, Double lat, Double lng, List<RecommendPlaceModel> recommend_place, List<TraceRouteModel> trace_list) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.recommend_place = recommend_place;
        this.trace_list = trace_list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<RecommendPlaceModel> getRecommend_place() {
        return recommend_place;
    }

    public void setRecommend_place(List<RecommendPlaceModel> recommend_place) {
        this.recommend_place = recommend_place;
    }

    public List<TraceRouteModel> getTrace_list() {
        return trace_list;
    }

    public void setTrace_list(List<TraceRouteModel> trace_list) {
        this.trace_list = trace_list;
    }
}
