package com.pizzastudio.centerpoint.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TraceResult {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name; // station name
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lng")
    private Double lng;

    @JsonProperty("recommend_place")
    private List<RecommendPlace> recommendPlaceList;
    @JsonProperty("trace_list")
    private List<Trace> traceList;

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

    public List<RecommendPlace> getRecommendPlaceList() {
        return recommendPlaceList;
    }

    public void setRecommendPlaceList(List<RecommendPlace> recommendPlaceList) {
        this.recommendPlaceList = recommendPlaceList;
    }

    public List<Trace> getTraceList() {
        return traceList;
    }

    public void setTraceList(List<Trace> traceList) {
        this.traceList = traceList;
    }

    @Override
    public String toString() {
        return "TraceResult{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", recommendPlaceList=" + recommendPlaceList +
                ", traceList=" + traceList +
                '}';
    }
}
