package com.example.weatherforcast.CallApi;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("weather")
    Call<JsonObject> getWeatherData(@Query("appid") String api_key, @Query("q") String city_name, @Query("units") String units);


}