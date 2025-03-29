package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.RecommendPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SubwayPlacesService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> countPlacesByName(String subwayNames){
        List<Map<String, Object>> items = jdbcTemplate.queryForList(
                " select subway_name, count(1) as count\n" +
                        "from subway_recommend_places\n" +
                        "where subway_name in ("+subwayNames+")\n" +
                        "group by subway_name\n" +
                        "order by 2 desc "
                );

        return items;
    }

    public List<RecommendPlace> findPlacesByName(String subwayName){
        List<RecommendPlace> items = jdbcTemplate.query(
                " select * \n" +
                        "from subway_recommend_places\n" +
                        "where subway_name like '"+subwayName+"역'\n"
        , new RecommendPlaceMapper());
        return items;
    }


}
