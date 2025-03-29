package com.pizzastudio.centerpoint.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Place {
    @JsonIgnore
    private Integer id;
    @JsonProperty("place_id")
    private String placeid;
    @JsonProperty("osm_type")
    private String osmType;
    @JsonProperty("osm_id")
    private String osmId;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lon")
    private Double lon;
    @JsonProperty("display_name")
    private String name;
    @JsonProperty("address")
    private Map<String,String> address;
    @JsonIgnore
    private Integer placeSearchStringId;
    @JsonProperty("place_search_string")
    private String placeSearchString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlaceSearchStringId() {
        return placeSearchStringId;
    }

    public void setPlaceSearchStringId(Integer placeSearchStringId) {
        this.placeSearchStringId = placeSearchStringId;
    }

    public String getPlaceSearchString() {
        return placeSearchString;
    }

    public void setPlaceSearchString(String placeSearchString) {
        this.placeSearchString = placeSearchString;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    public String getOsmId() {
        return osmId;
    }

    public void setOsmId(String osmId) {
        this.osmId = osmId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }
}
