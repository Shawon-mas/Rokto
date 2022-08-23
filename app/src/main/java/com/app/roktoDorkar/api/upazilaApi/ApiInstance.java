package com.app.roktoDorkar.api.upazilaApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInstance {
    private static String BASE_URL = " https://bloodbag.org/";
    private static Retrofit retrofit;

    private static Retrofit getRetrofitInstance()
    {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
        return retrofit;
    }
    public static DivisionApiEndPoint getDivisionApiEndpoint()
    {
        DivisionApiEndPoint divisionApiEndPoint =getRetrofitInstance().create(DivisionApiEndPoint.class);
        return divisionApiEndPoint;
    }

}
