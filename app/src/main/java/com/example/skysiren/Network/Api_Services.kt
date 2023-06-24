package com.example.skysiren.Network

import retrofit2.http.GET

interface Api_Services {
    @GET("onecall")
    suspend fun getWeather()
}