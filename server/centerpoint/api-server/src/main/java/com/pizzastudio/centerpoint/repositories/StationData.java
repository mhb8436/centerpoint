package com.pizzastudio.centerpoint.repositories;

import com.pizzastudio.centerpoint.model.Station;
import com.pizzastudio.centerpoint.model.StationTransit;
import com.pizzastudio.centerpoint.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class StationData {
    @Autowired
    private StationService stationService;

    public static String[] TableNames = new String[]{"stn_seoul", "stn_daejon", "stn_daegu", "stn_gangju", "stn_busan"};
    public int[][][] adjlines;
    boolean isInit = false;
    HashMap<Integer, HashMap<String, Station>> kMap;
    HashMap<Integer, ArrayList<Station>> stations;
    int maxIndex = 0;
    HashMap<Integer, HashMap<Integer, Station>> nMap;

    public int getMaxIndex() {
        return this.maxIndex;
    }

    @PostConstruct
    public void init() {
        adjlines = new int[5][][];
        int maxIndex = 0;
        this.nMap = new HashMap<Integer, HashMap<Integer,Station>>();
        this.kMap = new HashMap<Integer, HashMap<String,Station>>();
        this.stations = new HashMap<Integer, ArrayList<Station>>();
        this.load();
    }


    public void setData(Integer city, ArrayList<Station> list) {
        System.out.println("setData begin");
        this.stations.put(city, list);

        Iterator it = list.iterator();
        while (it.hasNext()) {
            Station s = (Station) it.next();
            HashMap map = this.nMap.get(city);
            if(map == null){
               map = new HashMap();
            }
            map.put(Integer.valueOf(s.no), s);
            HashMap map2 = this.kMap.get(city);
            if(map2 == null){
                map2 = new HashMap();
            }
            map2.put(s.name, s);
            this.nMap.put(city, map);
            this.kMap.put(city, map2);
            if (s.no > maxIndex) {
                maxIndex = s.no;
            }
        }
        this.isInit = true;
    }


    public Station findByName(Integer city, String name) {
        return (Station) this.kMap.get(city).get(name);
    }

    public Station findByNumber(Integer city, int no) {
        if (this.nMap == null) {
            return null;
        }
        return (Station) this.nMap.get(city).get(Integer.valueOf(no));
    }

    public ArrayList<Station> getList(Integer city) {
        return this.stations.get(city);
    }


    public String toDisplayText(Integer city, Station st, int line) {
        return String.format("%s (%s)", new Object[]{st.name, getLineName(city, line)});
    }

    public String getLineName(Integer city, int line) {
        String ret = "";
        if (city == 0 && line > 9) {
            if (line == 10) {
                ret = "경의·중앙선";
            }
            if (line == 11) {
                ret = "인천 1호선";
            }
            if (line == 12) {
                ret = "분당선";
            }
            if (line == 13) {
                ret = "공항선";
            }
            if (line == 14) {
                ret = "중앙선";
            }
            if (line == 15) {
                ret = "경춘선";
            }
            if (line == 16) {
                ret = "신분당선";
            }
            if (line == 17) {
                ret = "에버라인";
            }
            if (line == 18) {
                ret = "의정부 경전철";
            }
            if (line == 19) {
                ret = "수인선";
            }
            if (line == 20) {
                ret = "자기부상";
            } if (line == 21) {
                ret = "인천 2호선";
            }
            if (line == 22) {
                ret = "경강선";
            }
            if (line == 23) {
                return "우이신설선";
            }

            return ret;
        } else if (city != 4 || line <= 9) {
            return line + "호선";
        } else {
            if (line == 10) {
               ret = "부산-김해 경전철";
            }
            if (line == 11) { return "동해선"; }
            return ret;
        }
    }

    private void load(){
        for(int i=0;i<this.TableNames.length;i++){
            System.out.println("StationData load begin: [" + this.TableNames[i] + "]");
            List<Station> items = stationService.getStations(this.TableNames[i]);
            System.out.println("result is " + items.toString());
            setData(i+1, (ArrayList<Station>) items);
            List<StationTransit> adjline_itmes = stationService.getAdjlines(this.TableNames[i]);
            setAdjLines(i, adjline_itmes);

        }

    }

    private void setAdjLines(Integer city, List<StationTransit> adjline_itmes) {
        System.out.println("setAdjLines begin");
        this.adjlines[city] = new int[1000][1000];
        Iterator<StationTransit> items = adjline_itmes.iterator();
        while(items.hasNext()){
            StationTransit st = items.next();
            for(int i=0;i<st.getAdjlines().length;i++){
                this.adjlines[city][Integer.parseInt(st.getLineno())][i] = st.getAdjlines()[i];
            }

        }
    }

    public Station findByNearest(Integer city, double lat, double lon){
        List<Station> items = this.stations.get(city);
        double minDist = 999999999d;
        int minIdx = 0;
        for(int i=0;i<items.size();i++){
           Station st = items.get(i);
           double curDist = stationService.distance(lat, lon, st.getLat(), st.getLng(), 'M');
           minIdx = minDist > curDist ? i : minIdx;
           if(minDist > curDist){
              minDist = curDist; minIdx = i;
           }
        }
//        double finalMinDist = stationService.distance(lat, lon, items.get(minIdx).getLat(), items.get(minIdx).getLng(), 'M');
//        if(finalMinDist > 1000){
//            return null;
//        }
        return items.get(minIdx);
    }

    public Station findByNearest(double lat, double lon){
        double minDist = 999999999d;
        int minIdx = 0;

        List<Station> stations = new ArrayList<>();
        for(int i=1;i<=this.TableNames.length;i++){
            stations.add(findByNearest(i, lat,lon));
        }
        System.out.println(String.format(" stations size is %d", stations.size()));
        for(int i=0;i<stations.size();i++){
            System.out.println(String.format("idx : %d, station : ", i, stations.get(i)));
            double curDist = stationService.distance(lat, lon, stations.get(i).getLat(), stations.get(i).getLng(), 'M');
            minIdx = minDist > curDist ? i : minIdx;
            if(minDist > curDist){
                minDist = curDist; minIdx = i;
            }
        }
        double finalMinDist = stationService.distance(lat, lon, stations.get(minIdx).getLat(), stations.get(minIdx).getLng(), 'M');
        if(finalMinDist > 1000){
            return null;
        }
        return stations.get(minIdx);
    }

    public int findCityByStation(Station station){
        Set<Integer> cities = stations.keySet();
        Iterator it = cities.iterator();
        while(it.hasNext()){
            Integer city = (Integer)it.next();
            List<Station> items = stations.get(city);
            for(int j=0;j<items.size();j++){
                if(items.get(j).equals(station)){
                    return city;
                }
            }
        }
        return -1;
    }


}
