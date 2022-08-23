package com.app.roktoDorkar.api.upazilaApi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DivisionApiEndPoint {
  @GET("location")
    Call<DivisionModel> getDivision();

  @FormUrlEncoded
  @POST("location/division")
  Call<DistrictModel> getDistrict(@Field("division") Integer id);

}
