package com.example.skysiren.Model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeRepository : RepositoryInterface {
    private val WeatherList = MutableStateFlow<List<WeatherDetail>>(emptyList())

    private val WeatherListFavourite = MutableStateFlow<List<FavouritWeather>>(emptyList())


    override suspend fun getWeatherFromRetrofit(
        lat: Double?,
        lon: Double?,
        units: String,
        lang: String,
        apiKey: String,
    ): Flow<WeatherDetail>? {
        TODO("Not yet implemented")
    }

    override fun getWeatherFromRoom(): Flow<List<FavouritWeather>> {
        return WeatherListFavourite
    }

    override fun getAllAlertsFromRoom(): Flow<List<Alerts>> {
        TODO("Not yet implemented")
    }

    override fun getOfflineweather(): Flow<List<WeatherDetail>> {
        return WeatherList
    }

    override suspend fun insertWeather(weatherDetail: WeatherDetail) {

        WeatherList.value = listOf(weatherDetail)
    }

    override suspend fun insertWeatherToRoom(weather: FavouritWeather) {
        WeatherListFavourite.value= listOf(weather)
    }

    override suspend fun deletWeatherFromRoom(weather: FavouritWeather) {
        val filteredList = WeatherListFavourite.value.filter { it.equals(weather)  }
        WeatherListFavourite.value = filteredList
    }

    override suspend fun insertAlertToRoom(alert: Alerts) {
        TODO("Not yet implemented")
    }

    override suspend fun deletAlertFromRoom(alert: Alerts) {
        TODO("Not yet implemented")
    }
}