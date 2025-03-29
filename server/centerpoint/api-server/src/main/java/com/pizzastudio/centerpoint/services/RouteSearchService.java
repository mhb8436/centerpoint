package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.RouteData;
import com.pizzastudio.centerpoint.model.RouteResult;
import com.pizzastudio.centerpoint.model.Station;
import com.pizzastudio.centerpoint.repositories.StationData;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteSearchService {
    public static int defaultTransferWeight = 8;
    public static int defaultTransferWeightPath = 2;
    @Autowired
    public StationData stationData;
    private boolean isDebugMode = false;
    class MinTrans {
        private int[][] _adj;
        private int[] _destLines;
        private int _limit;
        private ArrayList<int[]> _results;
        private int[] _v = new int[15];

        MinTrans() {
        }

        private boolean isDestnation(int city, int line) {
            for (int n : this._destLines) {
                if (n == line) {
                    return true;
                }
            }
            return false;
        }

        protected ArrayList<int[]> getMinTrans(int city, int startLine, Station dest) {
            this._results = new ArrayList();
            System.out.println("-------- getMinTrans ------ ");
            System.out.println(dest);
            System.out.println("-------- getMinTrans ------ ");
            this._destLines = dest.lineno;
            if (isDestnation(city, startLine)) {
                this._results.add(new int[]{startLine});
                return this._results;
            }
            this._adj = RouteSearchService.this.stationData.adjlines[city];
            this._limit = this._adj.length - 2;
            this._v[0] = startLine;
            dfsAdjSearch(city, startLine, 1);
            return this._results;
        }

        private void dfsAdjSearch(int city, int cur, int depth) {
            if (depth <= this._limit) {
                int i;
                for (int b : this._adj[cur]) {
                    if (isDestnation(city, b)) {
                        int[] res = new int[(depth + 1)];
                        for (i = 0; i < depth; i++) {
                            res[i] = this._v[i];
                        }
                        res[depth] = b;
                        if (depth < this._limit) {
                            this._results.clear();
                            this._limit = depth;
                        }
                        this._results.add(res);
                        return;
                    }
                }
                for (int b2 : this._adj[cur]) {
                    boolean isContinue = true;
                    for (i = 1; i < depth; i++) {
                        if (b2 == this._v[i]) {
                            isContinue = false;
                        }
                    }
                    if (isContinue) {
                        this._v[depth] = b2;
                        dfsAdjSearch(city, b2, depth + 1);
                    }
                }
            }
        }
    }

    public void setDebugMode(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }

    public RouteResult findShortestPath(Station start, Station dest, int startLineNo) {
        int city = -1;
        int sCity = stationData.findCityByStation(start);
        int dCity = stationData.findCityByStation(dest);
        if(sCity == dCity){
            return findShortestPath(sCity, start, dest);
        }else{
            return null;
        }
    }

    public RouteResult findShortestPath(int city, Station start, Station dest, int startLineNo) {
        return findShortestPath(city, start, dest);
    }

    public RouteResult findShortestPath(int city, Station start, Station dest) {
        RouteData.isMinTransfer = false;
        RouteResult result = null;
        long sTm = System.currentTimeMillis();

//        System.out.println("findShortestPath 1 >> elapsed time : " + (System.currentTimeMillis() - sTm)/1000);
        for (int startLineNo : start.lineno) {
            RouteResult resultCur = createResultShortest(bfs(city, start, dest, startLineNo));
            if (result == null) {
                result = resultCur;
            } else if (result.compareTo(resultCur) > 0) {
                result = resultCur;
            }
        }
//        System.out.println("findShortestPath 2 >> elapsed time : " + (System.currentTimeMillis() - sTm)/1000);
//        RouteResult r2 = findMinTransPath(city, start, dest);
//        System.out.println("findShortestPath 3 >> elapsed time : " + (System.currentTimeMillis() - sTm)/1000);
//        if (this.isDebugMode) {
//            System.out.println("Route 1 : " + result);
//            System.out.println("Route 2 : " + r2);
//        }
//        if (r2 != null && result.getMinutes() - r2.getMinutes() > 0) {
//            return r2;
//        }
//        RouteData.isMinTransfer = false;
        return result;
    }

    public RouteResult findMinTransPath(int city, Station start, Station dest, int startLineNo) {
        return findMinTransPath(city, start, dest);
    }

    public RouteResult findMinTransPath(int city, Station start, Station dest) {
        RouteData.isMinTransfer = true;
        RouteResult result = null;
        for (int startLineNo : start.lineno) {
            ArrayList<RouteData> trace = minTransPath(city, start, dest, startLineNo);
            if (!(trace == null || trace.size() == 0)) {
                RouteResult resultCur = createResult(city, trace);
                if (result == null) {
                    result = resultCur;
                } else if (result.compareTo(resultCur) > 0) {
                    result = resultCur;
                }
            }
        }
        return result;
    }

    private ArrayList<Station> bfs(int city, Station start, Station dest, int startLineNo) {
        Station cur;
//        System.out.println(this.stationData.getList(city).size());
//        System.out.println("MAX INDEX : " + this.stationData.getMaxIndex());
        boolean[] isVisited = new boolean[5120];
        Arrays.fill(isVisited, false);
//        System.out.println(start.name);
        PriorityQueue<RouteData> queue = new PriorityQueue();
        queue.add(new RouteData(start.no, 0, startLineNo));
        HashMap<Integer, RouteData> path = new HashMap();
        while (queue.size() > 0) {
            RouteData elem = (RouteData) queue.poll();
            if (elem.no == dest.no) {
                RouteData best = elem;
                break;
            }
            cur = this.stationData.findByNumber(city, elem.no);
            if (this.isDebugMode) {
                System.out.println(this.stationData.findByNumber(city, elem.prevNo).name + "->" + cur.name + " queue " + queue.size() + " path " + path.size() + " w : " + elem.weight);
            }
            isVisited[cur.no] = true;
            int idx = -1;
            for (int adjstn : cur.adjstn) {
                RouteData rd;
                idx++;
                if (!isVisited[adjstn]) {
                    Station next = this.stationData.findByNumber(city, adjstn);
                    int w = elem.weight + cur.adjweight[idx];
                    int curLine = elem.curLine;
                    boolean sameLine = false;
                    for (int i : next.lineno) {
                        if (curLine == i) {
                            sameLine = true;
                            break;
                        }
                    }
                    rd = new RouteData(next.no, cur.no, w, curLine, curLine);
                    if (sameLine) {
                        int nextLineNoSel = curLine;
                    } else {
                        rd.tc++;
                        rd.weight += defaultTransferWeightPath;
                    }
                    queue.add(rd);
                    if (((RouteData) path.get(Integer.valueOf(next.no))) == null) {
                        path.put(Integer.valueOf(next.no), rd);
                    }
                }
            }
        }
        cur = dest;
//        if (this.isDebugMode) {
//            System.out.print(cur.name);
//        }
        ArrayList<Station> trace = new ArrayList();
        trace.add(dest);
        do {
            RouteData rd = (RouteData) path.get(Integer.valueOf(cur.no));
            if (rd != null) {
                cur = this.stationData.findByNumber(city, rd.prevNo);
                if (cur == null) {
                    break;
                }
                trace.add(cur);
            } else {
                break;
            }
        } while (cur != start);
        return trace;
    }

    private ArrayList<RouteData> minTransPath(int city, Station start, Station dest, int startLineNo) {
        Station cur;
        //StationData data = StationData.getData();
        ArrayList<Station> list = this.stationData.getList(city);
        long sTm = System.currentTimeMillis();
        ArrayList<int[]> lineResults = new MinTrans().getMinTrans(city, startLineNo, dest);
//        System.out.println("minTransPath [1]: elasped time " + (System.currentTimeMillis() - sTm )/1000 );
        StringBuilder b = new StringBuilder(IOUtils.LINE_SEPARATOR_UNIX);
        b.append(start + " -> " + dest + " : ");
        Iterator it = lineResults.iterator();
        while (it.hasNext()) {
            for (int n : (int[]) it.next()) {
                b.append(n).append(" ");
            }
            b.append("\n");
        }
        System.out.println(b);
        System.out.println("minTransPath [2]: elasped time " + (System.currentTimeMillis() - sTm )/1000 );
        ArrayList<Integer> destLineNos = new ArrayList();
        for (int valueOf : dest.lineno) {
            destLineNos.add(Integer.valueOf(valueOf));
        }
        PriorityQueue<RouteData> queue = new PriorityQueue();
        boolean[] isVisited = new boolean[5120];
        Arrays.fill(isVisited, false);
        queue.add(new RouteData(start.no, 0, startLineNo));
        HashMap<Integer, RouteData> path = new HashMap();
        while (queue.size() > 0) {
            System.out.println("queue size => " + queue.size());
            RouteData elem = (RouteData) queue.poll();
            if (elem.no == dest.no) {
                RouteData best = elem;
                break;
            }
            cur = this.stationData.findByNumber(city, elem.no);
            isVisited[cur.no] = true;
            int idx = -1;
            for (int adjstn : cur.adjstn) {
                RouteData rd;
                idx++;
                if (!isVisited[adjstn]) {
                    Station next = this.stationData.findByNumber(city, adjstn);
                    int w = elem.weight + cur.adjweight[idx];
                    int curLine = elem.nextLine;
                    if (curLine != 6 || !cur.name.equals("불광") || !next.name.equals("연신내") || !destLineNos.contains(Integer.valueOf(6))) {
                        for (int nextLineNo : next.lineno) {
                            rd = new RouteData(next.no, cur.no, w, curLine, nextLineNo);
                            rd.tc = elem.tc;
                            if (curLine != nextLineNo) {
                                boolean nextFound = false;
                                it = lineResults.iterator();
                                while (it.hasNext()) {
                                    int[] lineResult = (int[]) it.next();
                                    if (lineResult.length > elem.tc + 1 && lineResult[elem.tc + 1] == nextLineNo) {
                                        for (int nnos : next.lineno) {
                                            if (nnos == lineResult[elem.tc]) {
                                                nextFound = true;
                                            }
                                        }
                                    }
                                }
                                if (nextFound) {
                                    rd.weight += defaultTransferWeightPath;
                                    rd.tc++;
                                    rd.nextLine = nextLineNo;
                                } else {
                                }
                            }
                            if (!queue.contains(rd)) {
                                queue.add(rd);
                            }
                            if (((RouteData) path.get(Integer.valueOf(next.no))) == null) {
                                path.put(Integer.valueOf(next.no), rd);
                            }
                        }
                    } // end of curLine != 6
                }
            }
        }
        System.out.println("minTransPath [3]: elasped time " + (System.currentTimeMillis() - sTm )/1000 );
        cur = dest;
        if (this.isDebugMode) {
            System.out.print(cur.name);
        }
        ArrayList<RouteData> trace = new ArrayList();
        do {
            RouteData rd = (RouteData) path.get(Integer.valueOf(cur.no));
            if (rd != null) {
                cur = this.stationData.findByNumber(city, rd.prevNo);
                if (cur == null) {
                    break;
                }
                trace.add(rd);
            } else {
                break;
            }
        } while (cur != start);
        return trace;
    }

    public RouteResult createResult(Integer city, ArrayList<RouteData> trace) {
        RouteData cur;
        RouteResult result = new RouteResult();
        if (trace.size() > 0) {
            cur = (RouteData) trace.get(trace.size() - 1);
            result.setStartLineNo(cur.curLine);
            int curLineNo = ((RouteData) trace.get(trace.size() - 1)).curLine;

            System.out.println("----- createResult ------");
            result.addRouteData(new RouteResult.Data(stationData.findByNumber(city, cur.prevNo), cur.curLine, 0));
            for (int i = trace.size() - 1; i > 0; i--) {
                cur = (RouteData) trace.get(i);
                RouteData next = (RouteData) trace.get(i - 1);
                curLineNo = cur.curLine;
                result.addRouteData(new RouteResult.Data(stationData.findByNumber(city, cur.no), next.curLine, cur.weight));
            }
        }
        cur = (RouteData) trace.get(0);
        Station d = stationData.findByNumber(city, cur.no);
        result.addRouteData(new RouteResult.Data(stationData.findByNumber(city, cur.no), cur.curLine, cur.weight));
        result.setMinutes(cur.weight);
        result.setTcnt(cur.tc);
        return result;
    }

    public RouteResult createResultShortest(ArrayList<Station> trace) {
        RouteResult result = new RouteResult();
        int minutes = 0;
        int tcnt = 0;
        int curLineNo = -1;
        int nextLineNo = 0;
        int i = 0;
        while (i < trace.size()) {
            int nextW = 0;
            Station cur = (Station) trace.get(i);
            Station next = i < trace.size() + -1 ? (Station) trace.get(i + 1) : null;
            boolean isDiff = true;
            ArrayList<Integer> lineNos = new ArrayList();
            for (int curLineC : cur.lineno) {
                if (next != null) {
                    for (int nextLineC : next.lineno) {
                        if (curLineC == nextLineC) {
                            lineNos.add(Integer.valueOf(nextLineC));
                        }
                    }
                }
            }
            if (curLineNo == -1 && lineNos.size() > 0) {
                curLineNo = ((Integer) lineNos.get(0)).intValue();
            }
            for (int curLineC2 : cur.lineno) {
                if (next != null) {
                    Iterator it = lineNos.iterator();
                    while (it.hasNext()) {
                        if (curLineC2 == ((Integer) it.next()).intValue()) {
                            nextLineNo = curLineC2;
                            isDiff = false;
                            break;
                        }
                    }
                    continue;
                }
            }
            if (next != null) {
                for (int adjI = 0; adjI < cur.adjstn.length; adjI++) {
                    if (cur.adjstn[adjI] == next.no) {
                        nextW += cur.adjweight[adjI];
                    }
                }
            }
            if (isDiff) {
                nextW += RouteSearchService.defaultTransferWeight;
                tcnt++;
            }
            result.addRouteData(new RouteResult.Data(cur, curLineNo, nextW));
            minutes += nextW;
            curLineNo = nextLineNo;
            i++;
        }
        Collections.reverse(result.getRouteData());
        result.setMinutes(minutes);
        result.setTcnt(tcnt);
        return result;
    }


}
