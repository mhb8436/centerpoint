package com.pizzastudio.centerpoint.services;


import com.pizzastudio.centerpoint.model.PlaceSearchString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class PlaceSearchStringService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PlaceSearchString> list() {
        List<PlaceSearchString> items = jdbcTemplate.query("SELECT id,name from place_search_string ", new PlaceSearchStringMapper());

        return items;
    }

    public PlaceSearchString get(int id) {
        PlaceSearchString item = null;

        List<PlaceSearchString> books =
                jdbcTemplate.query(
                        "SELECT id, name from place_search_string  WHERE id = ?",
                        new Object[] { id },
                        new PlaceSearchStringMapper()
                );
        if (books.size() > 0) {
            item = books.get(0);
        }

        return item;
    }

    public PlaceSearchString get(String name) {
        PlaceSearchString item = null;

        List<PlaceSearchString> books =
                jdbcTemplate.query(
                        "SELECT id, name from place_search_string  WHERE name = ?",
                        new Object[] { name },
                        new PlaceSearchStringMapper()
                );
        if (books.size() > 0) {
            item = books.get(0);
        }

        return item;
    }


    public PlaceSearchString create(final PlaceSearchString item) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO place_search_string (name) VALUES (?)",
                        new String[] { "id"  }
                );

                preparedStatement.setString(1, item.getName());

                return preparedStatement;
            }
        };
        jdbcTemplate.update(
                preparedStatementCreator,
                keyHolder
        );
        System.out.println("PlaceSearchString create : " + keyHolder.getKey().intValue());
        return get(keyHolder.getKey().intValue());
    }


    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM place_search_string WHERE id = ?",
                new Object[] { id }
        );
    }
}
