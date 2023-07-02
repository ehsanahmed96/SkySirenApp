package com.example.skysiren.DataBase

import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.WeatherDetail
import com.example.skysiren.Network.ApiState

sealed class RoomState{
    class Success(val weather : List<FavouritWeather>) : RoomState()
    class Failure(val msg :Throwable) : RoomState()
    object Loading : RoomState()

}
