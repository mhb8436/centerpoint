package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.Station;
import com.pizzastudio.centerpoint.util.Util;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StationMapper implements RowMapper<Station> {

    @Override
    public Station mapRow(ResultSet rs, int rowNum) throws SQLException {
        //System.out.println("StationMapper begin");
        Station station = new Station();
        station.setNo(rs.getInt("id"));
        station.setLineno(Util.StringArrToIntArr(rs.getString("lineno").split(",")));
        if(rs.getString("adjstn") == null){
           station.setAdjstn(null);
        }else{
            station.setAdjstn(Util.StringArrToIntArr(rs.getString("adjstn").split(",")));
        }
        if(rs.getString("adjweight") == null){
           station.setAdjweight(null);
        }else{
            station.setAdjweight(Util.StringArrToIntArr(rs.getString("adjweight").split(",")));
        }
        station.setName(rs.getString("name"));
        station.setPosx(rs.getInt("x"));
        station.setPosy(rs.getInt("y"));
        station.setLat(rs.getDouble("lat"));
        station.setLng(rs.getDouble("lng"));
        try{
            station.setPlaceCount(rs.getInt("placeCount"));
        }catch(SQLException se){}

        return station;
    }
}
