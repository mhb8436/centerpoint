package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.Dong;
import com.pizzastudio.centerpoint.model.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DongService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Dong> listDongInCircle(Double lat, Double lng, Double distance){
        List<Dong> items = this.jdbcTemplate.query("select emd_cd, emd_eng_nm, emd_kor_nm, lat, lng, placeCount\n" +
                "from (\n" +
                "         select a.*,\n" +
                "                (select count(1) from dong_recommend_places p where a.emd_cd = p.emd_cd)                as placeCount,\n" +
                "                ST_PointInsideCircle(ST_Transform(ST_SetSRID(ST_Point(lng,lat),4326),32652),ST_X(ST_Transform(ST_GeomFromText('POINT("+lng+" "+lat+")',4326),32652)),ST_Y(ST_Transform(ST_GeomFromText('POINT("+lng+" "+lat+")',4326),32652)),"+distance+"::numeric) as cont\n" +
                "         from (\n" +
                "                  select emd_cd,\n" +
                "                         emd_eng_nm,\n" +
                "                         emd_kor_nm,\n" +
                "                         ST_X(ST_Transform(ST_SetSrid(ST_Centroid(geom), 5178), 4326)) as lng,\n" +
                "                         ST_Y(ST_Transform(ST_SetSrid(ST_Centroid(geom), 5178), 4326)) as lat\n" +
                "                  from tl_scco_emd) a\n" +
                "         where 1 = 1) x\n" +
                "where x.cont is true", new DongMapper());
        return items;
    }
}
