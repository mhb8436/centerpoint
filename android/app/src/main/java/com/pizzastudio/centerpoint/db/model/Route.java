package com.pizzastudio.centerpoint.db.model;

public class Route {

    // meet_id, participant_id, start_point, dest_point, duration, transit, encoded_polyline
    public static final String TABLE_NAME = "route";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PARTICIPANT_ID = "participant_id";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_TRANSIT = "transit";
    public static final String COLUMN_ENCODED_POLYLINE = "encoded_polyline";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private int participant_id;
    private long duration;
    private String transit;
    private String encoded_polyline;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PARTICIPANT_ID + " INTEGER,"
                    + COLUMN_DURATION + " REAL,"
                    + COLUMN_TRANSIT + " REAL,"
                    + COLUMN_ENCODED_POLYLINE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Route() {
    }

    public Route(int id, int participant_id, long duration, String transit, String encoded_polyline, String timestamp) {
        this.id = id;
        this.participant_id = participant_id;
        this.duration = duration;
        this.transit = transit;
        this.encoded_polyline = encoded_polyline;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(int participant_id) {
        this.participant_id = participant_id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTransit() {
        return transit;
    }

    public void setTransit(String transit) {
        this.transit = transit;
    }

    public String getEncoded_polyline() {
        return encoded_polyline;
    }

    public void setEncoded_polyline(String encoded_polyline) {
        this.encoded_polyline = encoded_polyline;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
