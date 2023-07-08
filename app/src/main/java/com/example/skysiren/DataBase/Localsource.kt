package com.example.skysiren.DataBase

import com.example.skysiren.Model.Alerts
import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.WeatherDetail
import kotlinx.coroutines.flow.Flow

interface Localsource {
    fun getWeatherFromRoom(): Flow<List<FavouritWeather>>
    fun getAllAlertsFromRoom() : Flow <List<Alerts>>

    suspend fun insertWeatherToRoom(weather: FavouritWeather)
    suspend fun deleteWeatherFromRoom(weather: FavouritWeather)

    suspend fun insertAlertToRoom(alert:Alerts)
    suspend fun deletAlertFromRoom(alert:Alerts)

    fun getOfflineWeather(): Flow<List<WeatherDetail>>
    suspend fun insertWeather(weatherDetail: WeatherDetail)
}