package com.pizzastudio.centerpoint.db.model;

public class Place {

    public static final String TABLE_NAME = "place";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DISPLAY_NAME = "display_name";
    public static final String COLUMN_PLACE_ID = "place_id";
    public static final String COLUMN_LATITUDE= "latitude";
    public static final String COLUMN_LONGITUDE= "longitude";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_SEARCH_STRING = "search_string";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String display_name;
    private String place_id;
    private String search_string;
    private Double latitude;
    private Double longitude;
    private String address;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DISPLAY_NAME + " TEXT,"
                    + COLUMN_PLACE_ID + " TEXT,"
                    + COLUMN_SEARCH_STRING + " TEXT,"
                    + COLUMN_LATITUDE + " NUMERIC(11,8),"
                    + COLUMN_LONGITUDE + " NUMERIC(11,8),"
                    + COLUMN_ADDRESS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Place() {
    }

    public Place(int id, String display_name, String place_id, String search_string, Double latitude, Double longitude, String address, String timestamp) {
        this.id = id;
        this.display_name = display_name;
        this.place_id = place_id;
        this.search_string = search_string;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getSearch_string() {
        return search_string;
    }

    public void setSearch_string(String search_string) {
        this.search_string = search_string;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", display_name='" + display_name + '\'' +
                ", place_id='" + place_id + '\'' +
                ", search_string='" + search_string + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
