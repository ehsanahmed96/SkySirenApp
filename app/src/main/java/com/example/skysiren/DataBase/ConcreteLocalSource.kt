package com.example.skysiren.DataBase

import android.content.Context

class ConcreteLocalSource (context: Context) : Localsource {
    private lateinit var weatherDAO: WeatherDAO

    init {
       // weatherDAO = WeatherDataBase.getInstance(context).getWeatherDao()
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
}