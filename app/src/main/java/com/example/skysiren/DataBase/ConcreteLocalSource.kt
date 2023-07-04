package com.example.skysiren.DataBase

import android.content.Context
import com.example.skysiren.Model.Alerts
import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.WeatherDetail
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource (context: Context) : Localsource {
    private val weatherDAO: WeatherDAO by lazy {
        val dataBase :WeatherDataBase = WeatherDataBase.getInstance(context)
        dataBase.getWeatherDao()
    }

    companion object  {
        private var instance: ConcreteLocalSource? = null
        fun getInstance(context: Context): ConcreteLocalSource {
            return instance ?: synchronized(this){
                val Instance = ConcreteLocalSource(context)
                instance =  Instance
                Instance}
        }
    }

    override fun getWeatherFromRoom(): Flow<List<FavouritWeather>> {
      return weatherDAO.getWeatherFromRoom()
    }


    override suspend fun insertWeatherToRoom(weather: FavouritWeather) {
        return weatherDAO.insertWeatherToRoom(weather)
    }

    override suspend fun deleteWeatherFromRoom(weather: FavouritWeather) {
        return weatherDAO.deleteWeatherFromRoom(weather)
    }

    override fun getAllAlertsFromRoom(): Flow<List<Alerts>> {
        return weatherDAO.getAlertsFromRoom()
    }

    override suspend fun insertAlertToRoom(alert: Alerts) {
       return weatherDAO.insertAlertToRoom(alert)
    }

    override suspend fun deletAlertFromRoom(alert: Alerts) {
       return weatherDAO.deleteAlertFromRoom(alert)
    }
}