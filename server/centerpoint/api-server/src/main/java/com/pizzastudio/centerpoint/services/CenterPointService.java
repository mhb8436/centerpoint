package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.CartesianPoint;
import com.pizzastudio.centerpoint.model.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CenterPointService {

    public Point calcCenterPoint(List<Point> pointList, List<Double> weightList){
        List<CartesianPoint> newPointList = new ArrayList<>();
        boolean nullweight = false;
        if(weightList == null || weightList.size() < pointList.size()) {
            weightList = new ArrayList<>();
            nullweight = true;
        }

        for(Point p : pointList){
            newPointList.add(new CartesianPoint(p.getLat(), p.getLng()));
            if(nullweight) weightList.add(1d);
        }
        Double x1 = 0.0d, y1=0.0d, z1=0.0d;
        Double totWeight = weightList.stream().mapToDouble(i->i.doubleValue()).sum();
        for(int i=0;i<newPointList.size();i++){
            x1 += newPointList.get(i).getX() * weightList.get(i);
            y1 += newPointList.get(i).getY() * weightList.get(i);
            z1 += newPointList.get(i).getZ() * weightList.get(i);
        }
        Double cx = x1/totWeight, cy = y1/totWeight, cz = z1/totWeight;
        Double newLon = Math.atan2(cy,cx);
        Double hyp = Math.sqrt(cx*cx+cy*cy);
        Double newLat = Math.atan2(cz, hyp);
        System.out.println((new Point(Math.toDegrees(newLat), Math.toDegrees(newLon), "CenterPoint")).toString());
        return new Point(Math.toDegrees(newLat), Math.toDegrees(newLon), "CenterPoint");
//        return null;
    }

}
