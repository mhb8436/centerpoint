package com.pizzastudio.centerpoint.model;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchPlaceServices {
    @GET("api/place/search?format=json&addressdetails=1")
    Call<ArrayList<SearchPlaceModel>> getFakePlaceBasedOnASearchTag(@Query("q") String tag);

    @GET("api/place/reverse")
    Call<ArrayList<SearchPlaceModel>> reverseGeoCoding(@Query("lat") Double latitude, @Query("lon") Double longitude);

}
