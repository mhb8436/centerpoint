package com.pizzastudio.centerpoint.model;

import java.io.Serializable;

public class StationTransit implements Serializable {
    private String lineno;
    private int[] adjlines;

    public String getLineno() {
        return lineno;
    }

    public void setLineno(String lineno) {
        this.lineno = lineno;
    }

    public int[] getAdjlines() {
        return adjlines;
    }

    public void setAdjlines(int[] adjlines) {
        this.adjlines = adjlines;
    }
}
