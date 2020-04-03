package com.example.weatherforecast

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/forecast?")
    fun getCurrentWeatherData(
        @Query("lat") lat: String, @Query("lon") lon: String,
        @Query("appid") api_key: String,
        @Query("units") units: String
    ): Call<WeatherForecast>
}