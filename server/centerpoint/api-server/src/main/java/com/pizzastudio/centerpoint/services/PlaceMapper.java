package com.pizzastudio.centerpoint.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzastudio.centerpoint.model.Place;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PlaceMapper implements RowMapper<Place> {
    public Place mapRow(ResultSet rs, int rowNum) throws SQLException {
        Place place = new Place();
        place.setId(rs.getInt("id"));
        place.setPlaceid(rs.getString("place_id"));
        place.setPlaceid(rs.getString("place_id"));
        place.setOsmType(rs.getString("osm_type"));
        place.setOsmId(rs.getString("osm_id"));
        place.setLat(rs.getDouble("lat"));
        place.setLon(rs.getDouble("lon"));
        place.setName(rs.getString("display_name"));
        place.setPlaceSearchStringId(rs.getInt("place_search_string_id"));
        place.setPlaceSearchString(rs.getString("place_search_string"));
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> addressMap = new HashMap<String, String>();
        JsonParser jsonParser = null;
        try {
            jsonParser = new JsonFactory().createParser(rs.getString("address"));
            addressMap =  mapper.readValue(jsonParser, HashMap.class);
            place.setAddress(addressMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return place;
    }
}
