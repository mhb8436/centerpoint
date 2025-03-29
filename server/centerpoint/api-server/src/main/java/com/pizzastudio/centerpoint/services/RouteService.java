package com.pizzastudio.centerpoint.services;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.pizzastudio.centerpoint.model.Trace;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class RouteService {
    GraphHopper hopper = new GraphHopperOSM().forServer();
    public RouteService(){
        ClassPathResource osmResource = new ClassPathResource("static/south-korea-latest.osm.pbf");
        try {
            System.out.println("classPathResource.getFile().toPath().toString() => " + osmResource.getFile().toPath().toString());
            hopper.setDataReaderFile(osmResource.getFile().toPath().toString());
            hopper.setGraphHopperLocation("/tmp/graphopper");
            hopper.setEncodingManager(new EncodingManager("car"));
            hopper.importOrLoad();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public long calcPath(final double fromLat, final double fromLon, final double toLat, final double toLon){
        GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon).
                setWeighting("fastest").
                setVehicle("car").
                setLocale(Locale.KOREA);
        GHResponse rsp = hopper.route(req);
        if(rsp.hasErrors()) {
            return -1;
        }
        PathWrapper path = rsp.getBest();
        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        long timeInMs = path.getTime();
        InstructionList il = path.getInstructions();
        List<Map<String, Object>> iList = il.createJson();
        PointList pl = path.getPoints();
        return timeInMs/1000/60;
    }


    public List<Trace> getPath(final double fromLat, final double fromLon, final double toLat, final double toLon){
        GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon).
                setWeighting("fastest").
                setVehicle("car").
                setLocale(Locale.KOREA);
        GHResponse rsp = hopper.route(req);
        if(rsp.hasErrors()) {
            return null;
        }
        PathWrapper path = rsp.getBest();
//        PointList pointList = path.getPoints();
//        double distance = path.getDistance();
        long timeInMs = path.getTime();
        InstructionList il = path.getInstructions();
        List<Map<String, Object>> iList = il.createJson();
        PointList pl = path.getPoints();
        List<Trace> traceList = new ArrayList<>();
        for(int i=0;i<pl.size();i++){
            long minute = (i==0)?timeInMs/1000/60:0;
            traceList.add(new Trace(
                    i+1,
                    pl.getLatitude(i),
                    pl.getLongitude(i),
                    (int)minute
            ));
        }
        return traceList;
    }
}
