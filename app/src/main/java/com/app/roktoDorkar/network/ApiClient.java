package com.app.roktoDorkar.network;

import static com.app.roktoDorkar.utilites.Constants.KEY_SERVER_KEY;
import static com.app.roktoDorkar.utilites.Constants.REMOTE_MESSAGE_CONTENT_TYPE;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiClient {
    @Headers({"Authorization: "+KEY_SERVER_KEY,"Content-Type: "+REMOTE_MESSAGE_CONTENT_TYPE})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification pushNotification);

}
