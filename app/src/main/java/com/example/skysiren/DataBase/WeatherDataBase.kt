package com.example.skysiren.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.TypeConverter
import com.example.skysiren.Model.WeatherDetail

@Database(entities = [/*WeatherDetail::class, */FavouritWeather::class], version = 1)
//@TypeConverters(TypeConverter::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDAO

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance(context: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, WeatherDataBase::class.java, "weatherDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}