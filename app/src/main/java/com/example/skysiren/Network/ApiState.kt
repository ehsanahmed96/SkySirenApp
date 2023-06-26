package com.example.skysiren.Network

import com.example.skysiren.Model.WeatherDetail

sealed class ApiState{
    class Success(val weather : WeatherDetail) : ApiState()
    class Failure(val msg :Throwable) :ApiState()
    object Loading : ApiState()
}
