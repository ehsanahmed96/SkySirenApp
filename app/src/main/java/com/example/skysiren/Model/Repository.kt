package com.example.skysiren.Model

import com.example.skysiren.DataBase.Localsource
import com.example.skysiren.Network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Repository private constructor(
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

    override suspend fun insertWeatherToRoom(weather: FavouritWeather) {
        return local.insertWeatherToRoom(weather)
    }

    override suspend fun deletWeatherFromRoom(weather: FavouritWeather) {
        return local.deleteWeatherFromRoom(weather)
    }
}