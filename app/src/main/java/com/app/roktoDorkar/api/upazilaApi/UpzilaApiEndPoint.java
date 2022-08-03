package com.app.roktoDorkar.api.upazilaApi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UpzilaApiEndPoint {
  @GET("upazila")
    Call<UpzilaModel> getUpazila();
  @FormUrlEncoded
  @POST("upazila/info")
  Call<DisDivModel> getDisDiv(@Field("id") Integer id);

}
