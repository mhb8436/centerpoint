package com.pizzastudio.centerpoint.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Dong {

    @JsonProperty("emd_cd")
    private String emdCd;
    @JsonProperty("emd_eng_nm")
    private String emdEngNm;
    @JsonProperty("emd_kor_nm")
    private String emdKorNm;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lng")
    private Double lng;

    @JsonIgnore
    public double tmpMean;
    @JsonIgnore
    public double tmpSD;
    @JsonIgnore
    public int placeCount;


    public String getEmdCd() {
        return emdCd;
    }

    public void setEmdCd(String emdCd) {
        this.emdCd = emdCd;
    }

    public String getEmdEngNm() {
        return emdEngNm;
    }

    public void setEmdEngNm(String emdEngNm) {
        this.emdEngNm = emdEngNm;
    }

    public String getEmdKorNm() {
        return emdKorNm;
    }

    public void setEmdKorNm(String emdKorNm) {
        this.emdKorNm = emdKorNm;
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

    public double getTmpMean() {
        return tmpMean;
    }

    public void setTmpMean(double tmpMean) {
        this.tmpMean = tmpMean;
    }

    public double getTmpSD() {
        return tmpSD;
    }

    public void setTmpSD(double tmpSD) {
        this.tmpSD = tmpSD;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }
}
