package com.example.skysiren.Model

import com.example.skysiren.DataBase.Localsource
import com.example.skysiren.Network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Repository(
    var local: Localsource,
    var remote: RemoteSource
) : RepositoryInterface {



    companion object {
        private var instance: Repository? = null
        fun getInstance(remote: RemoteSource, local: Localsource): Repository {
            return instance ?: synchronized(this) {
                val Instance = Repository(local, remote)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun getWeatherFromRetrofit(
        lat: Double?,
        lon: Double?,
        units: String,
        lang: String,
        apiKey: String,
    ): Flow<WeatherDetail>? {
        return flowOf(remote.getWeather(lat,lon,units,lang, apiKey))
    }

    override fun getWeatherFromRoom(): Flow<List<FavouritWeather>> {
        return local.getWeatherFromRoom()
    }

    override fun getAllAlertsFromRoom(): Flow<List<Alerts>> {
        return local.getAllAlertsFromRoom()
    }

    override fun getOfflineweather(): Flow<List<WeatherDetail>> {
        return local.getOfflineWeather()
    }

    override suspend fun insertWeather(weatherDetail: WeatherDetail) {
       return local.insertWeather(weatherDetail)
    }

    override suspend fun insertWeatherToRoom(weather: FavouritWeather) {
        return local.insertWeatherToRoom(weather)
    }

    override suspend fun deletWeatherFromRoom(weather: FavouritWeather) {
        return local.deleteWeatherFromRoom(weather)
    }

    override suspend fun insertAlertToRoom(alert: Alerts) {
        return local.insertAlertToRoom(alert)
    }

    override suspend fun deletAlertFromRoom(alert: Alerts) {
        return local.deletAlertFromRoom(alert)
    }
}