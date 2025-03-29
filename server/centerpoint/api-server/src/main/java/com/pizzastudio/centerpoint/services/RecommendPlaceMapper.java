package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.RecommendPlace;
import com.pizzastudio.centerpoint.model.Station;
import com.pizzastudio.centerpoint.util.Util;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecommendPlaceMapper implements RowMapper<RecommendPlace> {

    @Override
    public RecommendPlace mapRow(ResultSet rs, int rowNum) throws SQLException {
        //System.out.println("StationMapper begin");
        RecommendPlace item = new RecommendPlace();
        item.setId(rs.getInt("id"));
        item.setTitle(rs.getString("title"));
        item.setDetail(rs.getString("detail"));
        try{
            item.setLat(rs.getDouble("lat"));
        }catch(SQLException se){item.setLat(0.0d);}
        try{
            item.setLng(rs.getDouble("lng"));
        }catch(SQLException se){item.setLng(0.0d);}
        item.setLocation(rs.getString("location"));
        try {
            item.setScore(Double.parseDouble(rs.getString("score")));
        }catch(Exception e){item.setScore(0.0d);}

        item.setThumb_link(rs.getString("thumb_link"));
        item.setThumb_img(rs.getString("thumb_img"));

        return item;
    }
}
