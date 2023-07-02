package com.example.skysiren.DataBase

import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.WeatherDetail
import kotlinx.coroutines.flow.Flow

interface Localsource {
    fun getWeatherFromRoom(): Flow<List<FavouritWeather>>
    suspend fun insertWeatherToRoom(weather: FavouritWeather)
    suspend fun deleteWeatherFromRoom(weather: FavouritWeather)
}