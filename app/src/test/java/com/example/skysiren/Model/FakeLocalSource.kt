package com.example.skysiren.Model

import com.example.skysiren.DataBase.Localsource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalSourcee (
    var AlertsList: MutableList<Alerts> = mutableListOf(),
    var favWeatherList: MutableList<FavouritWeather> = mutableListOf(),
    var weatherDetailList: MutableList<WeatherDetail> = mutableListOf()) :Localsource{

    override fun getWeatherFromRoom(): Flow<List<FavouritWeather>> {
        return flowOf(favWeatherList)
    }

    override fun getAllAlertsFromRoom(): Flow<List<Alerts>> {
        return flowOf(AlertsList)
    }

    override suspend fun insertWeatherToRoom(weather: FavouritWeather) {
        favWeatherList.add(weather)
    }

    override suspend fun deleteWeatherFromRoom(weather: FavouritWeather) {
        favWeatherList.remove(weather)
    }

    override suspend fun insertAlertToRoom(alert: Alerts) {
        AlertsList.add(alert)
    }

    override suspend fun deletAlertFromRoom(alert: Alerts) {
        AlertsList.remove(alert)
    }

    override fun getOfflineWeather(): Flow<List<WeatherDetail>> {
        return flowOf(weatherDetailList)
    }

    override suspend fun insertWeather(weatherDetail: WeatherDetail) {
        weatherDetailList.add(weatherDetail)
    }
}