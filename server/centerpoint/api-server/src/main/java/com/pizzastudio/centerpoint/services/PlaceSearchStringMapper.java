package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.PlaceSearchString;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaceSearchStringMapper implements RowMapper<PlaceSearchString> {
    public PlaceSearchString mapRow(ResultSet rs, int rowNum) throws SQLException {
        PlaceSearchString place = new PlaceSearchString();
        place.setId(rs.getInt("id"));
        place.setName(rs.getString("name"));
        return place;
    }
}
