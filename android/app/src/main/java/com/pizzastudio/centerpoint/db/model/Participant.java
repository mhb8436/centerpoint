package com.pizzastudio.centerpoint.db.model;

public class Participant {

    public static final String TABLE_NAME = "participant";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_PLACE_ID = "place_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_MEET_ID = "meet_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String name;
    private String place;
    private String type;
    private int place_id;
    private double latitude;
    private double longitude;
    private String address;
    private int meet_id;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS  " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_TYPE + " TEXT,"
                    + COLUMN_PLACE + " TEXT,"
                    + COLUMN_PLACE_ID + " INTEGER,"
                    + COLUMN_LATITUDE + " REAL,"
                    + COLUMN_LONGITUDE + " REAL,"
                    + COLUMN_ADDRESS + " TEXT,"
                    + COLUMN_MEET_ID + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Participant() {
    }

    public Participant(int id, String name, String type, String place, int place_id, double latitude, double longitude, String address, int meet_id, String timestamp) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.place = place;
        this.place_id = place_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.meet_id = meet_id;
        this.timestamp = timestamp;
    }

    // getter && setter


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMeet_id() {
        return meet_id;
    }

    public void setMeet_id(int meet_id) {
        this.meet_id = meet_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", type='" + type + '\'' +
                ", place_id=" + place_id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", meet_id=" + meet_id +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
