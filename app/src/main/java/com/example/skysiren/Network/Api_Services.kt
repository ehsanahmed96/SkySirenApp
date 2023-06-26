package com.example.skysiren.Network

import com.example.skysiren.Model.WeatherDetail
import retrofit2.http.GET
import retrofit2.http.Query

interface Api_Services {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String, // stands for language
        @Query("appid") apiKey: String,
    ): WeatherDetail

}