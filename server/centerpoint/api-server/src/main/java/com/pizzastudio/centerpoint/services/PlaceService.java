package com.pizzastudio.centerpoint.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzastudio.centerpoint.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class PlaceService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Place> getBooks() {
        List<Place> items = jdbcTemplate.query("SELECT a.id, a.place_id, a.osm_type, a.osm_id, a.display_name, a.lat, a.lon, a.address, b.id as place_search_string_id , b.name  as place_search_string from places a, place_search_string b where a.place_search_string_id = b.id  ", new PlaceMapper());

        return items;
    }

    public List<Place> getBooks(int place_search_string_id) {
        List<Place> items = jdbcTemplate.query(
                "SELECT a.id, a.place_id, a.osm_type, a.osm_id, a.display_name, a.lat, a.lon, a.address, b.id as place_search_string_id , b.name  as place_search_string from places a, place_search_string b where a.place_search_string_id = b.id and b.id = ? ",
                new Object[] { place_search_string_id },
                new PlaceMapper());

        return items;
    }

    public Place getBook(int id) {
        Place item = null;

        List<Place> books =
                jdbcTemplate.query(
                        "SELECT a.id, a.place_id, a.osm_type, a.osm_id, a.display_name, a.lat, a.lon, a.address, b.id as place_search_string_id , b.name  as place_search_string from places a, place_search_string b where a.place_search_string_id = b.id and  a.id = ?",
                        new Object[] { id },
                        new PlaceMapper()
                );
        if (books.size() > 0) {
            item = books.get(0);
        }

        return item;
    }

    public Place create(final Place item) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO places (place_id, osm_type,osm_id, display_name, lat, lon, address, place_search_string_id,place_search_string) VALUES (?, ?,?, ?, ?, ?, ?::JSON,?,?)",
                        new String[] { "id"  }
                );
                int idx = 1;
                preparedStatement.setString(idx++, item.getPlaceid());
                preparedStatement.setString(idx++, item.getOsmType());
                preparedStatement.setString(idx++, item.getOsmId());
                preparedStatement.setString(idx++, item.getName());
                preparedStatement.setDouble(idx++, item.getLat());
                preparedStatement.setDouble(idx++, item.getLon());
                String address = "";
                try {
                    address = new ObjectMapper().writeValueAsString(item.getAddress());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                preparedStatement.setString(idx++, address);
                preparedStatement.setInt(idx++,item.getPlaceSearchStringId());
                preparedStatement.setString(idx++,item.getPlaceSearchString());

                return preparedStatement;
            }
        };
        jdbcTemplate.update(
                preparedStatementCreator,
                keyHolder
        );

        return getBook(keyHolder.getKey().intValue());
    }


    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM places WHERE id = ?",
                new Object[] { id }
        );
    }
}
