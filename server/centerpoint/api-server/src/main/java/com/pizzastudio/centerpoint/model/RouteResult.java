package com.pizzastudio.centerpoint.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RouteResult implements Comparable<RouteResult>, Serializable {
    private static final long serialVersionUID = -7218276905523863123L;
    @JsonProperty("minutes")
    private int minutes;
    @JsonProperty("routeData")
    ArrayList<Data> routeData;
    @JsonProperty("startLineNo")
    int startLineNo;
    @JsonProperty("tcnt")
    private int tcnt;

    public List<Trace> getTraceData() {
        int no = 1;
        List<Trace> traceList = new ArrayList<>();
        for(Data data: this.routeData){
            traceList.add(new Trace(
                    no,
                    data.getStation().getLat(),
                    data.getStation().getLng(),
                    data.minute
            ));
        }
        return traceList;
    }

    public static class Data {
        @JsonProperty("lineno")
        int lineno;
        @JsonProperty("minute")
        int minute;
        //@JsonProperty("stn")
        Station stn;

        public Data(Station stn, int lineno, int minute) {
            this.stn = stn;
            this.lineno = lineno;
            this.minute = minute;
        }

        public String toString() {
            return String.format("stn:%s line:%d m:%d", new Object[]{this.stn, Integer.valueOf(this.lineno), Integer.valueOf(this.minute)});
        }

        public Station getStation() {
            return this.stn;
        }
    }

    public RouteResult() {
        this.routeData = null;
        this.routeData = new ArrayList();
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setRouteData(ArrayList<Data> routeData) {
        this.routeData = routeData;
    }

    public void addRouteData(Data data){
        this.routeData.add(data);
    }
    public int getStartLineNo() {
        return startLineNo;
    }

    public void setStartLineNo(int startLineNo) {
        this.startLineNo = startLineNo;
    }

    public int getTcnt() {
        return tcnt;
    }

    public void setTcnt(int tcnt) {
        this.tcnt = tcnt;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public int getTransferCount() {
        return this.tcnt;
    }

    public ArrayList<Data> getRouteData() {
        return this.routeData;
    }

    public int compareTo(RouteResult another) {
        if (this.tcnt == another.tcnt) {
            return this.minutes - another.minutes;
        }
        return this.tcnt - another.tcnt;
    }

    public String toString() {
        return "RouteResult [startLineNo=" + this.startLineNo + ", minutes=" + this.minutes + ", tcnt=" + this.tcnt + ", routeData=" + this.routeData + "]";
    }
}
