package com.example.skysiren.Network

import com.example.skysiren.Model.WeatherDetail
import okhttp3.ResponseBody

interface RemoteSource {
    suspend fun getWeather(lat:Double?,lon:Double?,units:String,lang:String,apiKey:String): WeatherDetail

}