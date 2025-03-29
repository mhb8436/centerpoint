package com.pizzastudio.centerpoint.services;

import com.pizzastudio.centerpoint.model.Place;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface PlaceCrawler {

    // https://nominatim.openstreetmap.org/search?q=%EA%B2%BD%ED%9D%AC%EB%8C%80%ED%95%99%EA%B5%90&format=json&addressdetails=1
    @GET("/search")
    Call<List<Place>> getPlace(@Query("q") String q, @Query("format") String format, @Query("addressdetails") String addressdetails, @Query("countrycodes") String countrycodes);


    // https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=37.5281149&lon=126.8753029
    @GET("/reverse")
    Call<List<Place>> reverseGeocoding(@Query("format") String format, @Query("lat") String latitude, @Query("lon") String longitude);

}
