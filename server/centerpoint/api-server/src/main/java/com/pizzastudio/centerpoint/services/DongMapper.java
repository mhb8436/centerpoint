package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.Dong;
import com.pizzastudio.centerpoint.model.Station;
import com.pizzastudio.centerpoint.util.Util;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DongMapper implements RowMapper<Dong> {

    @Override
    public Dong mapRow(ResultSet rs, int rowNum) throws SQLException {

        Dong dong = new Dong();
        dong.setEmdCd(rs.getString("emd_cd"));
        dong.setEmdEngNm(rs.getString("emd_eng_nm"));
        dong.setEmdKorNm(rs.getString("emd_kor_nm"));
        dong.setLat(rs.getDouble("lat"));
        dong.setLng(rs.getDouble("lng"));
        try{
            dong.setPlaceCount(rs.getInt("placeCount"));
        }catch(Exception e){
            dong.setPlaceCount(0);
        }
        return dong;
    }
}
