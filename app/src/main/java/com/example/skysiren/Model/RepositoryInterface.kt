package com.example.skysiren.Model

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getWeatherFromRetrofit(lat:Double?,lon:Double?,units:String,lang:String,apiKey:String): Flow<WeatherDetail>?

    fun getWeatherFromRoom() : Flow<List<FavouritWeather>>
    fun getAllAlertsFromRoom() : Flow <List<Alerts>>
    fun getOfflineweather() : Flow <List<WeatherDetail>>

    suspend fun insertWeather(weatherDetail: WeatherDetail)

    suspend fun insertWeatherToRoom(weather: FavouritWeather)
    suspend fun deletWeatherFromRoom(weather : FavouritWeather)

    suspend fun insertAlertToRoom(alert:Alerts)
    suspend fun deletAlertFromRoom(alert:Alerts)
}