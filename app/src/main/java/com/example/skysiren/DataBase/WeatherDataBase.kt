package com.example.skysiren.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skysiren.Model.WeatherDetail

//@Database(entities = arrayOf(WeatherDetail::class) , version = 1)
abstract class WeatherDataBase{/* : RoomDatabase(){
    abstract fun getWeatherDao() : WeatherDAO
    companion object{
        @Volatile
        private var INSTANCE : WeatherDataBase? = null
        fun getInstance (context: Context) :WeatherDataBase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,WeatherDataBase::class.java , "weatherDB"
                ).build()
                INSTANCE=instance
                instance
            }
        }
    }*/
}