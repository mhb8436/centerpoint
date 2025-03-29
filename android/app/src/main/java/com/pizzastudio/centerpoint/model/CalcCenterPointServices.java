package com.pizzastudio.centerpoint.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CalcCenterPointServices {
    @POST("api/new/cp")
    Call<List<PointModel>> calcuation(@Body List<PointModel> pointList);

}
