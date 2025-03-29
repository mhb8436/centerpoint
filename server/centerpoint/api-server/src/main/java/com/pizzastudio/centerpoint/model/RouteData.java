package com.pizzastudio.centerpoint.model;

public class RouteData  implements  Comparable<RouteData>  {
    public static boolean isMinTransfer = true;
    public int curLine;
    public int nextLine;
    public int no;
    public int path;
    public int prevNo;
    public int tc;
    public int weight = 9999;

    public RouteData(int no, int weight, int curLine) {
        this.no = no;
        this.weight = weight;
        this.curLine = curLine;
        this.nextLine = curLine;
    }

    public RouteData(int no, int prevNo, int weight, int curLine, int nextLine) {
        this.no = no;
        this.prevNo = prevNo;
        this.weight = weight;
        this.curLine = curLine;
        this.nextLine = nextLine;
    }

    public int compareTo(RouteData o) {
        if (!isMinTransfer) {
            return this.weight - o.weight;
        }
        int tcc = this.tc - o.tc;
        if (tcc != 0) {
            return tcc;
        }
        return this.weight - o.weight;
    }

    public String toString() {
        return String.format("%d %d %d", new Object[]{Integer.valueOf(this.no), Integer.valueOf(this.weight), Integer.valueOf(this.tc)});
    }
}
