package com.example.skysiren.DataBase

import androidx.room.*
import com.example.skysiren.Model.Alerts
import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.WeatherDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {
    @Query("Select * FROM favourite")
    fun getWeatherFromRoom(): Flow<List<FavouritWeather>>
    @Query("Select * FROM Alerts")
    fun getAlertsFromRoom(): Flow<List<Alerts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherToRoom(weather : FavouritWeather)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlertToRoom(alert : Alerts)

    @Delete
    suspend fun deleteWeatherFromRoom(weather: FavouritWeather)
    @Delete
    suspend fun deleteAlertFromRoom(alert : Alerts)
}