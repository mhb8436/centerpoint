package com.pizzastudio.centerpoint.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzastudio.centerpoint.model.*;
import com.pizzastudio.centerpoint.repositories.StationData;
import com.pizzastudio.centerpoint.services.*;
import com.pizzastudio.centerpoint.util.Util;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;
import retrofit2.http.Path;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Controller
public class PlaceController {

    @Autowired
    private PlaceService placeService;
    @Autowired
    private PlaceSearchStringService placeSearchStringService;
    @Autowired
    private PlaceCrawler placeCrawler;
    @Autowired
    private SeleniumCrawler seleniumCrawler;
    @Autowired
    private RouteService routeService;
    @Autowired
    private RouteSearchService routeSearchService;
    @Autowired
    private StationService stationService;
    @Autowired
    private DongService dongService;
    @Autowired
    private CenterPointService centerPointService;
    @Autowired
    private SubwayPlacesService subwayPlacesService;

    @RequestMapping("/health_check")
    @ResponseBody
    public String health_check(){
        return "check";
    }
    @RequestMapping("/api/place/search")
    @ResponseBody
    public List<Place> getPlaces(@RequestParam("q") String name) {
        List<Place> items = new ArrayList<>();
        PlaceSearchString placeSearchString = placeSearchStringService.get(name);
        if(placeSearchString != null){
            items = placeService.getBooks(placeSearchString.getId());
        }else{
            Response<List<Place>>  results = null;
            try {
//                results = placeCrawler.getPlace(name, "json","1", "kr").execute();
                items  = seleniumCrawler.getPlace(name, "json","1", "kr");
            } catch (Exception e) {
                e.printStackTrace();
            }
//            items = results.body();
            PlaceSearchString sString = placeSearchStringService.create(new PlaceSearchString(name));
            for(Place item: items){
                item.setPlaceSearchStringId(sString.getId());
                item.setPlaceSearchString(sString.getName());
                placeService.create(item);
            }
        }

        return items;
    }

    @RequestMapping("/api/place/reverse")
    @ResponseBody
    public List<Place> reverseGeocoding(@RequestParam("lat") String latitude, @RequestParam("lon") String longitude ) {
        List<Place> items = new ArrayList<>();
        String name = latitude + ":" +longitude;
        PlaceSearchString placeSearchString = placeSearchStringService.get(name);
        if(placeSearchString != null){
            items = placeService.getBooks(placeSearchString.getId());
        }else{
            Response<List<Place>>  results = null;
            try {
//                results = placeCrawler.reverseGeocoding("jsonv2", latitude, longitude).execute();
                items  = seleniumCrawler.reverseGeocoding("jsonv2", latitude, longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            items = results.body();

            PlaceSearchString sString = placeSearchStringService.create(new PlaceSearchString(name));
            for(Place item: items){
                item.setPlaceSearchStringId(sString.getId());
                item.setPlaceSearchString(sString.getName());
                placeService.create(item);
            }
        }

        return items;
    }

    @RequestMapping("/api/distance/{fromLat}/{fromLon}/{toLat}/{toLon}")
    @ResponseBody
    public Long calcPath(@PathVariable double fromLat,
                          @PathVariable double fromLon,
                          @PathVariable double toLat,
                          @PathVariable double toLon) {

        //List<Map<String, Object>> aa = routeService.calcPath(fromLat,fromLon,toLat,toLon);
        return routeService.calcPath(fromLat,fromLon,toLat,toLon);
    }

    @RequestMapping("/api/metro/distance/{city}/{stnStart}/{stnEnd}")
    @ResponseBody
    public RouteResult calcMetroPath(@PathVariable int city,
                                     @PathVariable String stnStart,
                                     @PathVariable String stnEnd
    ){

        long startTm = System.currentTimeMillis();
        Station stnStartObj = routeSearchService.stationData.findByName(city, stnStart);
        Station stnEndObj = routeSearchService.stationData.findByName(city, stnEnd);
        RouteResult lastResultShortest = this.routeSearchService.findShortestPath(city, stnStartObj, stnEndObj, stnStartObj.lineno[0]);
        RouteResult bestResult = lastResultShortest;
        return bestResult;

    }

    @RequestMapping(value= "/api/centerpoint/{city}", method= RequestMethod.POST)
    @ResponseBody
    public Point calcCenterPoint(@PathVariable int city,
                                 @RequestBody List<Point> pointList){
        Point firstCP = centerPointService.calcCenterPoint(pointList, null);
        List<Double> weightList = new ArrayList<>();
        for(Point p : pointList){
            if(p.getType() != null && p.getType().equals("metro")){
                Station stnStartObj = routeSearchService.stationData.findByNearest(city, p.getLat(), p.getLng());
                Station stnEndObj = routeSearchService.stationData.findByNearest(city, firstCP.getLat(), firstCP.getLng());
                System.out.println(String.format("p.getLat(): %f, p.getLng(): %f, firstCP.getLat():%f, firstCP.getLng():%f ", p.getLat(), p.getLng(), firstCP.getLat(), firstCP.getLng()));
                System.out.println("stnStart:" + stnStartObj.getName());
                System.out.println("stnEnd:" + stnEndObj.getName());
                RouteResult bestResult =   this.routeSearchService.findShortestPath(city, stnStartObj, stnEndObj, stnStartObj.lineno[0]);
                weightList.add((double)bestResult.getMinutes());
            }else{
                weightList.add((double)routeService.calcPath(p.getLat(), p.getLat(), firstCP.getLat(), firstCP.getLng()));
            }
        }
        System.out.println(weightList);
        System.out.println("------ final weight -------");
        System.out.println(weightList);

        return centerPointService.calcCenterPoint(pointList, weightList);
    }

    @RequestMapping(value="/api/new/cp/{city}", method= RequestMethod.POST)
    @ResponseBody
    public List<Point> calcNewCenterPoint(@PathVariable int city,
                                    @RequestBody List<Point> pointList){

        Point firstCP = centerPointService.calcCenterPoint(pointList, null);
        Double minDist = 9999999999d;
        for(Point p: pointList){
            Double dist = stationService.distance(p.getLat(),p.getLng(), firstCP.getLat(), firstCP.getLng(), 'M');
            if(dist < minDist){
                minDist = dist;
            }
        }
        String tableNm = StationData.TableNames[city-1];
        System.out.println(String.format(" %s ,  %.2f,  %.2f ,  %.2f", tableNm, firstCP.getLat(), firstCP.getLng(), minDist));
        List<Station> nearbyStations = stationService.listStationInCircle(tableNm, firstCP.getLat(), firstCP.getLng(), minDist);
        System.out.println("minDist = " + minDist + "nearbyStations size =" + nearbyStations.size() );

        for(Station s: nearbyStations){
            Double[] distArray = new Double[pointList.size()];
            for(int i=0; i < pointList.size(); i++){
                Point p = pointList.get(i);
                if(p.getType().equals("metro")){
                    Station stnStartObj = routeSearchService.stationData.findByNearest(city, p.getLat(), p.getLng());
                    Station stnEndObj = routeSearchService.stationData.findByNearest(city, s.lat, s.lng);
                    RouteResult bestResult = null;
                    if(stnStartObj!=null && stnEndObj!=null){
                        bestResult =   this.routeSearchService.findShortestPath(city, stnStartObj, stnEndObj, stnStartObj.lineno[0]);
                    }
                    distArray[i] = new Double(bestResult.getMinutes());
                }else{
                    distArray[i] = new Double(routeService.calcPath(s.lat, s.lng, p.getLat(), p.getLng()));
                }
            }
            double[] thisValue = Util.calculateSD(distArray);;
            s.setTmpMean(thisValue[0]);
            s.setTmpSD(thisValue[1]);
            System.out.println("---------------");
            System.out.println(s.getName());
            System.out.println("---------------");
        }

        Collections.sort(nearbyStations, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                if(((o1.getTmpMean()+o1.getTmpSD()) - (o2.getTmpMean()+o2.getTmpSD())) > 0){
                    return 1;
                }else if(((o1.getTmpMean()+o1.getTmpSD()) - (o2.getTmpMean()+o2.getTmpSD())) < 0){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        for(Iterator<Station> it = nearbyStations.iterator() ; it.hasNext() ; ){
            Station s = it.next();
            if((s.getTmpMean() + s.getTmpSD()) < 10){
                it.remove();
            }
        }

        int TOP = nearbyStations.size() > 20 ? 20 : nearbyStations.size();
        List<Station> distTopList = new ArrayList<Station>(nearbyStations.subList(0, TOP));

        Collections.sort(distTopList, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                if(((int)((o2.getPlaceCount()) - (o1.getPlaceCount()))) > 0){
                    return 1;
                }else if(((int)((o2.getPlaceCount()) - (o1.getPlaceCount()))) < 0){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        System.out.println("TOP count"  + TOP);
        int TOP2 = distTopList.size() > 5 ? 5: distTopList.size();
        System.out.println("TOP2 count"  + TOP2);
        List<Station> totalTopList = new ArrayList<Station>(distTopList.subList(0, TOP2));
        for(Station s : totalTopList){
            System.out.println(String.format("%s    %.2f    %.2f    %.2f   %d ", s.getName(), s.getTmpMean(), s.getTmpSD(), (s.getTmpMean() + s.getTmpSD()), s.getPlaceCount()));
        }

        for(Point p : pointList){
            List<TraceResult> traceResultList = new ArrayList<>();
            int id = 1;
            for(Station s: totalTopList){
                TraceResult tr = new TraceResult();
                tr.setId(id++);
                tr.setName(s.getName());
                tr.setLat(s.getLat());
                tr.setLng(s.getLng());
                tr.setRecommendPlaceList(subwayPlacesService.findPlacesByName(s.getName()));

                if(p.getType().equals("metro")){
                    Station stnStartObj = routeSearchService.stationData.findByNearest(city, p.getLat(), p.getLng());
                    Station stnEndObj = routeSearchService.stationData.findByNearest(city, s.lat, s.lng);
                    RouteResult bestResult = null;
                    if(stnStartObj!=null && stnEndObj!=null){
                        bestResult =   this.routeSearchService.findShortestPath(city, stnStartObj, stnEndObj, stnStartObj.lineno[0]);
                    }

                    List<Trace> trList = bestResult.getTraceData();
                    tr.setTraceList(trList);
                }else{
                    tr.setTraceList(routeService.getPath(s.lat, s.lng, p.getLat(), p.getLng()));

                }
                traceResultList.add(tr);
            }
            p.setTr(traceResultList);
        }

        return pointList;
    }

    @RequestMapping(value="/api/new/cp", method= RequestMethod.POST)
    @ResponseBody
    public List<Point> calcNewCenterPoint(@RequestBody List<Point> pointList){
        Point firstCP = centerPointService.calcCenterPoint(pointList, null);
        Double minDist = 9999999999d;
        for(Point p: pointList){
            Double dist = stationService.distance(p.getLat(),p.getLng(), firstCP.getLat(), firstCP.getLng(), 'M');
            if(dist < minDist){
                minDist = dist;
            }
        }

        List<Station> nearbyStations = stationService.listStationInCircle(firstCP.getLat(), firstCP.getLng(), minDist);
        System.out.println(String.format("minDist is %f,  nearbyStations is %d", minDist, nearbyStations.size()));
        if(nearbyStations.size() > 0){
            return stationList(pointList, nearbyStations);
        }else{
            List<Dong> nearbyDong = dongService.listDongInCircle(firstCP.getLat(), firstCP.getLng(), minDist);
            return dongList(pointList, nearbyDong);
        }
    }

    private List<Point> dongList(List<Point> pointList, List<Dong> nearbyDong) {
        System.out.println(String.format("nearbyDong list size is %s", nearbyDong.size()));
        for(Dong d: nearbyDong){
            Double[] distArray = new Double[pointList.size()];
            for(int i=0; i < pointList.size(); i++){
                Point p = pointList.get(i);
                if(p.getType().equals("metro")){
                    Station stnStartObj = routeSearchService.stationData.findByNearest(p.getLat(), p.getLng());
                    Station stnEndObj = routeSearchService.stationData.findByNearest(d.getLat(), d.getLng());
                    RouteResult bestResult = null;
                    if(stnStartObj != null && stnEndObj != null){
                        bestResult =   this.routeSearchService.findShortestPath(stnStartObj, stnEndObj, stnStartObj.lineno[0]);
                    }
                    System.out.println(String.format("bestResult is %s", bestResult));
                    if(bestResult != null ){
                        distArray[i] = new Double(bestResult.getMinutes());
                    }else{
                        distArray[i] = new Double(routeService.calcPath(d.getLat(), d.getLng(), p.getLat(), p.getLng()));
                    }
                }else{
                    distArray[i] = new Double(routeService.calcPath(d.getLat(), d.getLng(), p.getLat(), p.getLng()));
                }
            }
            System.out.println(String.format("distArray size is %s", distArray.length));
            double[] thisValue = Util.calculateSD(distArray);;
            d.setTmpMean(thisValue[0]);
            d.setTmpSD(thisValue[1]);

        }

        Collections.sort(nearbyDong, new Comparator<Dong>() {
            @Override
            public int compare(Dong o1, Dong o2) {
                if(((o1.getTmpMean()+o1.getTmpSD()) - (o2.getTmpMean()+o2.getTmpSD())) > 0){
                    return 1;
                }else if(((o1.getTmpMean()+o1.getTmpSD()) - (o2.getTmpMean()+o2.getTmpSD())) < 0){
                    return -1;
                }else{
                    return 0;
                }
            }
        });

        for(Iterator<Dong> it = nearbyDong.iterator() ; it.hasNext() ; ){
            Dong s = it.next();
            if((s.getTmpMean() + s.getTmpSD()) < 10){
                it.remove();
            }
        }
        int TOP = nearbyDong.size() > 20 ? 20 : nearbyDong.size();
        List<Dong> distTopList = new ArrayList<Dong>(nearbyDong.subList(0, TOP));

        Collections.sort(distTopList, new Comparator<Dong>() {
            @Override
            public int compare(Dong o1, Dong o2) {
                if(((int)((o2.getPlaceCount()) - (o1.getPlaceCount()))) > 0){
                    return 1;
                }else if(((int)((o2.getPlaceCount()) - (o1.getPlaceCount()))) < 0){
                    return -1;
                }else{
                    return 0;
                }
            }
        });

        System.out.println("TOP count"  + TOP);
        int TOP2 = distTopList.size() > 5 ? 5: distTopList.size();
        System.out.println("TOP2 count"  + TOP2);
        List<Dong> totalTopList = new ArrayList<Dong>(distTopList.subList(0, TOP2));
        for(Dong s : totalTopList){
            System.out.println(String.format("%s    %.2f    %.2f    %.2f   %d ", s.getEmdKorNm(), s.getTmpMean(), s.getTmpSD(), (s.getTmpMean() + s.getTmpSD()), s.getPlaceCount()));
        }


        for(Point p : pointList){
            List<TraceResult> traceResultList = new ArrayList<>();
            int id = 1;
            for(Dong s: totalTopList){
                TraceResult tr = new TraceResult();
                tr.setId(id++);
                tr.setName(s.getEmdKorNm());
                tr.setLat(s.getLat());
                tr.setLng(s.getLng());
                tr.setRecommendPlaceList(subwayPlacesService.findPlacesByName(s.getEmdKorNm()));

                if(p.getType().equals("metro")){
                    Station stnStartObj = routeSearchService.stationData.findByNearest( p.getLat(), p.getLng());
                    Station stnEndObj = routeSearchService.stationData.findByNearest(s.getLat(), s.getLng());
                    RouteResult bestResult = null;
                    if(stnStartObj != null && stnEndObj != null){
                        bestResult =   this.routeSearchService.findShortestPath( stnStartObj, stnEndObj, stnStartObj.lineno[0]);
                    }

                    if(bestResult != null){
                        List<Trace> trList = bestResult.getTraceData();
                        tr.setTraceList(trList);
                    }else{
                        p.setType("car");
                        tr.setTraceList(routeService.getPath(s.getLat(), s.getLng(), p.getLat(), p.getLng()));
                    }
                }else{
                    tr.setTraceList(routeService.getPath(s.getLat(), s.getLng(), p.getLat(), p.getLng()));

                }
                traceResultList.add(tr);
            }
            p.setTr(traceResultList);
        }

        return pointList;
    }

    private List<Point> stationList(List<Point> pointList, List<Station> nearbyStations) {

        for(Station s: nearbyStations){
            Double[] distArray = new Double[pointList.size()];
            for(int i=0; i < pointList.size(); i++){
                Point p = pointList.get(i);
                if(p.getType().equals("metro")){
                    Station stnStartObj = routeSearchService.stationData.findByNearest(p.getLat(), p.getLng());
                    Station stnEndObj = routeSearchService.stationData.findByNearest(s.lat, s.lng);
                    RouteResult bestResult = null;
                    if(stnStartObj != null && stnEndObj != null){
                        bestResult =   this.routeSearchService.findShortestPath(stnStartObj, stnEndObj, stnStartObj.lineno[0]);
                    }
                    if(bestResult != null ){
                        distArray[i] = new Double(bestResult.getMinutes());
                    }else{
                        distArray[i] = new Double(routeService.calcPath(s.lat, s.lng, p.getLat(), p.getLng()));
                    }
                }else{
                    distArray[i] = new Double(routeService.calcPath(s.lat, s.lng, p.getLat(), p.getLng()));
                }
            }
            double[] thisValue = Util.calculateSD(distArray);;
            s.setTmpMean(thisValue[0]);
            s.setTmpSD(thisValue[1]);
        }

        Collections.sort(nearbyStations, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                if(((o1.getTmpMean()+o1.getTmpSD()) - (o2.getTmpMean()+o2.getTmpSD())) > 0){
                    return 1;
                }else if(((o1.getTmpMean()+o1.getTmpSD()) - (o2.getTmpMean()+o2.getTmpSD())) < 0){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        for(Iterator<Station> it = nearbyStations.iterator() ; it.hasNext() ; ){
            Station s = it.next();
            if((s.getTmpMean() + s.getTmpSD()) < 10){
                it.remove();
            }
        }

        int TOP = nearbyStations.size() > 20 ? 20 : nearbyStations.size();
        List<Station> distTopList = new ArrayList<Station>(nearbyStations.subList(0, TOP));

        Collections.sort(distTopList, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                if(((int)((o2.getPlaceCount()) - (o1.getPlaceCount()))) > 0){
                    return 1;
                }else if(((int)((o2.getPlaceCount()) - (o1.getPlaceCount()))) < 0){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        System.out.println("TOP count"  + TOP);
        int TOP2 = distTopList.size() > 5 ? 5: distTopList.size();
        System.out.println("TOP2 count"  + TOP2);
        List<Station> totalTopList = new ArrayList<Station>(distTopList.subList(0, TOP2));
        for(Station s : totalTopList){
            System.out.println(String.format("%s    %.2f    %.2f    %.2f   %d ", s.getName(), s.getTmpMean(), s.getTmpSD(), (s.getTmpMean() + s.getTmpSD()), s.getPlaceCount()));
        }

        for(Point p : pointList){
            List<TraceResult> traceResultList = new ArrayList<>();
            int id = 1;
            for(Station s: totalTopList){
                TraceResult tr = new TraceResult();
                tr.setId(id++);
                tr.setName(s.getName());
                tr.setLat(s.getLat());
                tr.setLng(s.getLng());
                tr.setRecommendPlaceList(subwayPlacesService.findPlacesByName(s.getName()));

                if(p.getType().equals("metro")){
                    Station stnStartObj = routeSearchService.stationData.findByNearest( p.getLat(), p.getLng());
                    Station stnEndObj = routeSearchService.stationData.findByNearest(s.lat, s.lng);
                    RouteResult bestResult = null;
                    if(stnStartObj != null && stnEndObj != null){
                        bestResult =   this.routeSearchService.findShortestPath( stnStartObj, stnEndObj, stnStartObj.lineno[0]);
                    }

                    if(bestResult != null){
                        List<Trace> trList = bestResult.getTraceData();
                        tr.setTraceList(trList);
                    }else{
                        p.setType("car");
                        tr.setTraceList(routeService.getPath(s.lat, s.lng, p.getLat(), p.getLng()));
                    }
                }else{
                    tr.setTraceList(routeService.getPath(s.lat, s.lng, p.getLat(), p.getLng()));

                }
                traceResultList.add(tr);
            }
            p.setTr(traceResultList);
        }
        return pointList;

    }


}
