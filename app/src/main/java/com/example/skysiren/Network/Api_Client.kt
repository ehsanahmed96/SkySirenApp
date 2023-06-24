package com.example.skysiren.Network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api_Client {
    private val api_service: Api_Services

    init {
        val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        val gson = GsonBuilder().create()
        val retrofitInstance = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        api_service = retrofitInstance.create(Api_Services::class.java)
    }
}