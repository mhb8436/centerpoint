package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.Station;
import com.pizzastudio.centerpoint.model.StationTransit;
import com.pizzastudio.centerpoint.util.Util;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StationTransitMapper implements RowMapper<StationTransit> {

    @Override
    public StationTransit mapRow(ResultSet rs, int i) throws SQLException {
//        Station station = new Station();
//        station.setNo(rs.getInt("no"));
//        station.setLineno(Util.StringArrToIntArr(rs.getString("lineno").split(",")));
//        station.setAdjstn(Util.StringArrToIntArr(rs.getString("adjstn").split(",")));
//        station.setAdjweight(Util.StringArrToIntArr(rs.getString("adjweight").split(",")));
//        station.setName(rs.getString("name"));
//        station.setPosx(rs.getInt("x"));
//        station.setPosy(rs.getInt("y"));
//        return station;

        StationTransit item = new StationTransit();
        item.setLineno(rs.getString("lineno"));
        //System.out.println((Integer[])rs.getArray("adjlines").getArray());
        item.setAdjlines(ArrayUtils.toPrimitive((Integer[])rs.getArray("adjlines").getArray()));

        return item;
    }
}
