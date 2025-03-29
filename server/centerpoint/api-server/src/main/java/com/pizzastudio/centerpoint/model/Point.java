package com.pizzastudio.centerpoint.model;

import java.util.List;

public class Point {
    private String name;
    private String type; // metro, car
    private Double lat;
    private Double lng;
    private List<TraceResult> tr;

    public Point(Double lat, Double lng, String name){
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
    public Point(Double lat, Double lng, String name, String type) {
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }

    public Point() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<TraceResult> getTr() {
        return tr;
    }

    public void setTr(List<TraceResult> tr) {
        this.tr = tr;
    }

    @Override
    public String toString() {
        return "Point{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", tr=" + tr +
                '}';
    }
}
