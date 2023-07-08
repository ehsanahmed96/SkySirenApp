package com.example.skysiren.HomeFragment.HomeView

import com.example.skysiren.Model.WeatherDetail
import com.example.skysiren.Network.ApiState

sealed class OfflineWeatherState{
    class Success(val weather :List<WeatherDetail>) : OfflineWeatherState()
    class Failure(val msg :Throwable) : OfflineWeatherState()
    object Loading : OfflineWeatherState()
}
