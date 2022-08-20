package com.app.roktoDorkar.firebase;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FCMSend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY ="key=AAAA_d-ovxo:APA91bH9bJ5DdSa7baKIIYerLr5Zj3n0xy9Oqv9vMoVHtyqeHgAGEKn1zvdZlvHrGqd_lAFzbj12nb9a-I8ENnN7Sl_qafdU38qP3xTb-y7KLuD3fDFO7DK-sQGwOwIIfRW4Cu1_hoep";

    public static void pushNotification(Context context,String token,String title,String message){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        try {
            JSONObject json = new JSONObject();
            json.put("to",token);
            JSONObject notification = new JSONObject();
            notification.put("title",title);
            notification.put("body",message);
            notification.put("notification",notification);
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                     System.out.println("FCM: "+ response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error: ",error.networkResponse.toString());

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                   Map<String, String> params = new HashMap<>();
                   params.put("Content-Type","application/json");
                   params.put("Authorization",SERVER_KEY);
                   return params;

                }
            };

        requestQueue.add(jsonObjectRequest);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
