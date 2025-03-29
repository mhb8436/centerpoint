package com.pizzastudio.centerpoint.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Arrays;

public class Station implements Serializable {
    private static final long serialVersionUID = 3957088999555007193L;
    @JsonIgnore
    public int[] adjstn;
    @JsonIgnore
    public int[] adjweight;
    @JsonProperty("lat")
    public double lat;
    @JsonIgnore
    public int[] lineno;
    @JsonIgnore
    public int linenoActive = 0;
    @JsonProperty("lng")
    public double lng;
    @JsonProperty("name")
    public String name;
    @JsonIgnore
    public Station nextStn;
    @JsonProperty("no")
    public int no;
    @JsonIgnore
    public int posx;
    @JsonIgnore
    public int posy;
    @JsonIgnore
    public String statnId = null;
    @JsonIgnore
    public double tmpMean;
    @JsonIgnore
    public double tmpSD;
    @JsonIgnore
    public int placeCount;

    public Station(Station from) {
        this.no = from.no;
        this.lineno = (int[]) from.lineno.clone();
        this.posx = from.posx;
        this.posy = from.posy;
        this.adjstn = (int[]) from.adjstn.clone();
        this.adjweight = (int[]) from.adjweight.clone();
        this.name = from.name;
        this.linenoActive = from.linenoActive;
        this.statnId = from.statnId;
        this.lat = from.lat;
        this.lng = from.lng;
    }

    public Station() {

    }

    public String toString() {
        return String.format("%d %s %d %d %d", new Object[]{Integer.valueOf(this.no), this.name, Integer.valueOf(this.lineno[0]), Integer.valueOf(this.posx), Integer.valueOf(this.posy)});
    }

    public int hashCode() {
        return ((((Arrays.hashCode(this.lineno) + 31) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + this.no;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Station other = (Station) obj;
        if (!Arrays.equals(this.lineno, other.lineno)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.no != other.no) {
            return false;
        }
        return true;
    }

    public int[] getAdjstn() {
        return adjstn;
    }

    public void setAdjstn(int[] adjstn) {
        this.adjstn = adjstn;
    }

    public int[] getAdjweight() {
        return adjweight;
    }

    public void setAdjweight(int[] adjweight) {
        this.adjweight = adjweight;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int[] getLineno() {
        return lineno;
    }

    public void setLineno(int[] lineno) {
        this.lineno = lineno;
    }

    public int getLinenoActive() {
        return linenoActive;
    }

    public void setLinenoActive(int linenoActive) {
        this.linenoActive = linenoActive;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Station getNextStn() {
        return nextStn;
    }

    public void setNextStn(Station nextStn) {
        this.nextStn = nextStn;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public String getStatnId() {
        return statnId;
    }

    public void setStatnId(String statnId) {
        this.statnId = statnId;
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

