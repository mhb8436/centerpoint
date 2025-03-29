package com.pizzastudio.centerpoint.db.model;

public class Restaurant {

    public static final String TABLE_NAME = "restaurant";
    // meet_id, point_id, recommend_id, restaurant_name, restaurant_type, restaurant_location, restaurant_price, restaurant_score
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_MEET_ID = "meet_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String type;
    private String address;
    private int price;
    private int score;
    private int meet_id;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_LATITUDE + " REAL,"
                    + COLUMN_LONGITUDE + " REAL,"
                    + COLUMN_TYPE + " TEXT,"
                    + COLUMN_ADDRESS + " TEXT,"
                    + COLUMN_PRICE + " INTEGER,"
                    + COLUMN_SCORE + " INTEGER,"
                    + COLUMN_MEET_ID + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Restaurant() {
    }

    public Restaurant(int id, String name, double latitude, double longitude, String type, String address, int price, int score, int meet_id, String timestamp) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.type = type;
        this.price = price;
        this.score = score;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
}
