package com.pizzastudio.centerpoint.db.model;

public class Meet {

    public static final String TABLE_NAME = "meet";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PREFER = "prefer";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String name;
    private String date;
    private String prefer;
    private String timestamp;

    private int participant_count;
    private String centerpoint_name;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_PREFER + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Meet() {
    }

    public Meet(int id, String name, String date, String prefer, String timestamp) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.prefer = prefer;
        this.timestamp = timestamp;
    }

    public Meet(int id, String name, String date, String prefer, String timestamp, int participant_count, String centerpoint_name) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.prefer = prefer;
        this.timestamp = timestamp;

        this.participant_count = participant_count;
        this.centerpoint_name = centerpoint_name;

    }

    public int getParticipant_count() {
        return participant_count;
    }

    public void setParticipant_count(int participant_count) {
        this.participant_count = participant_count;
    }

    public String getCenterpoint_name() {
        return centerpoint_name;
    }

    public void setCenterpoint_name(String centerpoint_name) {
        this.centerpoint_name = centerpoint_name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrefer() {
        return prefer;
    }

    public void setPrefer(String prefer) {
        this.prefer = prefer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
