package com.pizzastudio.centerpoint.model;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;
import ir.mirrajabi.searchdialog.core.Searchable;

public class SearchPlaceModel implements Searchable {
    @SerializedName("place_id")
    private String mPlaceid;
    @SerializedName("osm_type")
    private String mOsm_type;
    @SerializedName("osm_id")
    private String mOsm_id;
    @SerializedName("lat")
    private Double mLat;
    @SerializedName("lon")
    private Double mLon;
    @SerializedName("display_name")
    private String mName;
    @SerializedName("address")
    private JSONObject mAddress;
    public SearchPlaceModel(){

    }
    public SearchPlaceModel(String mPlaceid, String mOsm_type, String mOsm_id, Double mLat, Double mLon, String mName, JSONObject mAddress) {
        this.mPlaceid = mPlaceid;
        this.mOsm_type = mOsm_type;
        this.mOsm_id = mOsm_id;
        this.mLat = mLat;
        this.mLon = mLon;
        this.mName = mName;
        this.mAddress = mAddress;
    }

    @Override
    public String getTitle() {
        return mName;
    }
    public String getmPlaceid() {
        return mPlaceid;
    }

    public SearchPlaceModel setmPlaceid(String mPlaceid) {
        this.mPlaceid = mPlaceid;
        return this;
    }

    public String getmOsm_type() {
        return mOsm_type;
    }

    public SearchPlaceModel setmOsm_type(String mOsm_type) {
        this.mOsm_type = mOsm_type;
        return this;
    }

    public String getmOsm_id() {
        return mOsm_id;
    }

    public SearchPlaceModel setmOsm_id(String mOsm_id) {
        this.mOsm_id = mOsm_id;
        return this;
    }

    public Double getmLat() {
        return mLat;
    }

    public SearchPlaceModel setmLat(Double mLat) {
        this.mLat = mLat;
        return this;
    }

    public Double getmLon() {
        return mLon;
    }

    public SearchPlaceModel setmLon(Double mLon) {
        this.mLon = mLon;
        return this;
    }

    public String getmName() {
        return mName;
    }

    public SearchPlaceModel setmName(String mName) {
        this.mName = mName;
        return this;
    }

    public JSONObject getmAddress() {
        return mAddress;
    }

    public SearchPlaceModel setmAddress(JSONObject mAddress) {
        this.mAddress = mAddress;
        return this;
    }

    @Override
    public String toString() {
        return "SearchPlaceModel{" +
                "mPlaceid='" + mPlaceid + '\'' +
                ", mOsm_type='" + mOsm_type + '\'' +
                ", mOsm_id='" + mOsm_id + '\'' +
                ", mLat=" + mLat +
                ", mLon=" + mLon +
                ", mName='" + mName + '\'' +
                ", mAddress=" + mAddress +
                '}';
    }
}
