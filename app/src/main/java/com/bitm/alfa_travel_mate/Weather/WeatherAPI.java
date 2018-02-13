package com.bitm.alfa_travel_mate.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by Jibunnisa on 4/18/2017.
 */

public interface WeatherAPI {


    @GET("v1/public/yql")
    Call<WeatherData> getWeatherData(@Query("q") String query1, @Query("format") String query2);

}
