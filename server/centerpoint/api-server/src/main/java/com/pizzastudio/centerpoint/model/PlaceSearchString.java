package com.pizzastudio.centerpoint.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PlaceSearchString {
    @JsonIgnore
    private Integer id;
    @JsonProperty("name")
    private String name;

    public PlaceSearchString() {
    }

    public PlaceSearchString(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
