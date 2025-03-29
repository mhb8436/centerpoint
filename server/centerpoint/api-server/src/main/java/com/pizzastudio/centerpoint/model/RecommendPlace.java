package com.pizzastudio.centerpoint.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecommendPlace {
    @JsonProperty("id")
    private int id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("score")
    private Double score;
    @JsonProperty("detail")
    private String  detail;
    @JsonProperty("location")
    private String location;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lng")
    private Double lng;
    @JsonProperty("thumb_link")
    private String thumb_link;
    @JsonProperty("thumb_img")
    private String thumb_img;

    public RecommendPlace() {
    }

    public RecommendPlace(int id, String title, Double score, String detail, String location, Double lat, Double lng, String thumb_link, String thumb_img) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.detail = detail;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.thumb_link = thumb_link;
        this.thumb_img = thumb_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getThumb_link() {
        return thumb_link;
    }

    public void setThumb_link(String thumb_link) {
        this.thumb_link = thumb_link;
    }

    public String getThumb_img() {
        return thumb_img;
    }

    public void setThumb_img(String thumb_img) {
        this.thumb_img = thumb_img;
    }
}
