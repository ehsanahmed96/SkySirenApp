package com.example.skysiren.Model

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getWeatherFromRetrofit(lat:Double?,lon:Double?,units:String,lang:String,apiKey:String): Flow<WeatherDetail>?
    fun getWeatherFromRoom() : Flow<List<FavouritWeather>>
    suspend fun insertWeatherToRoom(weather: FavouritWeather)
    suspend fun deletWeatherFromRoom(weather : FavouritWeather)
}