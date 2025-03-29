package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.Station;
import com.pizzastudio.centerpoint.model.StationTransit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StationService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Station> getStations(String tablenm) {
//        List<Station> items = this.jdbcTemplate.query("SELECT * FROM " + tablenm, new StationMapper());
        List<Station> items = this.jdbcTemplate.query(String.format("select *, (select count(1) from subway_recommend_places p where a.name||'역' = p.subway_name and p.region = '%s') as placeCount from %s a", tablenm, tablenm), new StationMapper());
        return items;
    }

    public List<StationTransit> getAdjlines(String tablenm) {
        List<StationTransit> items  = jdbcTemplate.query("with linenos as (\n" +
                "  select * from generate_series(1,18)\n" +
                ")\n" +
                    "select generate_series::text as lineno ,array_sort_unique(array_concat_agg(string_to_array("+tablenm+".lineno,',')::int[])) as adjlines \n" +
                "from "+tablenm+", linenos\n" +
                "where 1 = 1\n" +
                "  and generate_series::text = any(string_to_array(lineno, ','))\n" +
                "group by generate_series::text\n" +
                "order by 1 asc", new StationTransitMapper());
        return items;
    }

    public List<Station> listStationInCircle(String tablenm, Double lat, Double lng, Double distance){
        List<Station> items = this.jdbcTemplate.query("select name,id,lineno,x,y,adjstn,adjweight,lat,lng,placeCount from (\n" +
                "                select\n" +
                "                       *,\n" +
                "                       (select count(1) from subway_recommend_places p where a.name||'역' = p.subway_name and p.region = 'seoul') as placeCount,\n"+
                "                       st_pointinsidecircle(ST_Point(lat, lng), "+lat+", "+lng+", ("+distance+"::numeric/111)) as cont\n" +
                "                from "+tablenm+" a \n" +
                "                where 1 = 1\n" +
                "              ) x\n" +
                "where 1=1\n" +
                "and x.cont is true", new StationMapper());
        return items;
    }

    // https://gis.stackexchange.com/questions/77072/return-all-results-within-a-30km-radius-of-a-specific-lat-long-point
    public List<Station> listStationInCircle(Double lat, Double lng, Double distance){

        List<Station> items = this.jdbcTemplate.query("select name,id,lineno,x,y,adjstn,adjweight,lat,lng,placeCount from (\n" +
                "                select\n" +
                "                       *,\n" +
                "                       (select count(1) from subway_recommend_places p where a.name||'역' = p.subway_name and p.region = a.region) as placeCount,\n"+
                "                       ST_PointInsideCircle(ST_Transform(ST_SetSRID(ST_Point(lng,lat),4326),32652),ST_X(ST_Transform(ST_GeomFromText('POINT("+lng+" "+lat+")',4326),32652)),ST_Y(ST_Transform(ST_GeomFromText('POINT("+lng+" "+lat+")',4326),32652)),"+distance+"::numeric) as cont\n" +
                "                from ((select 'seoul' as region, * from stn_seoul) union all (select 'busan' as region,* from stn_busan) union all (select 'daegu' as region,* from stn_daegu)  union all (select 'daejon' as region,* from stn_daejon) union all (select 'gangju' as region,* from stn_gangju)) a \n" +
                "                where 1 = 1\n" +
                "              ) x\n" +
                "where 1=1\n" +
                "and x.cont is true", new StationMapper());
        return items;
    }
//    original method 2019.08.15
//    public List<Station> listStationInCircle(Double lat, Double lng, Double distance){
//        List<Station> items = this.jdbcTemplate.query("select name,id,lineno,x,y,adjstn,adjweight,lat,lng,placeCount from (\n" +
//                "                select\n" +
//                "                       *,\n" +
//                "                       (select count(1) from subway_recommend_places p where a.name||'역' = p.subway_name and p.region = a.region) as placeCount,\n"+
//                "                       st_pointinsidecircle(ST_Point(lat, lng), "+lat+", "+lng+", ("+distance+"::numeric/111)) as cont\n" +
//                "                from ((select 'seoul' as region, * from stn_seoul) union all (select 'busan' as region,* from stn_busan) union all (select 'daegu' as region,* from stn_daegu)  union all (select 'daejon' as region,* from stn_daejon) union all (select 'gangju' as region,* from stn_gangju)) a \n" +
//                "                where 1 = 1\n" +
//                "              ) x\n" +
//                "where 1=1\n" +
//                "and x.cont is true", new StationMapper());
//        return items;
//    }


    public double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'M') {
            dist = dist * 1.609344 * 1000;
        }
        return (dist);
    }

}
