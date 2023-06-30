package com.example.skysiren.Network

import android.util.Log
import com.example.skysiren.Model.WeatherDetail
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api_Client : RemoteSource {
    private val api_service: Api_Services

    init {
        val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        val gson = GsonBuilder().create()
        val retrofitInstance = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        api_service = retrofitInstance.create(Api_Services::class.java)
    }

    override suspend fun getWeather(
        lat: Double?,
        lon: Double?,
        units: String,
        lang: String,
        apiKey: String,
    ): WeatherDetail {
        return api_service.getWeather(lat,lon,units, lang, apiKey)
    }

}