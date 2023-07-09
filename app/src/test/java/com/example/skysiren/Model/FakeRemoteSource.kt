package com.example.skysiren.Model

import com.example.skysiren.Network.RemoteSource

class FakeRemoteSource (val weatherDetail: WeatherDetail):RemoteSource{
    override suspend fun getWeather(
        lat: Double?,
        lon: Double?,
        units: String,
        lang: String,
        apiKey: String,
    ): WeatherDetail {
        WeatherDetail(30.2,30.5,"",1231, emptyList(),
            Current(0,0.0,0,0.0,1,1,1,
                1,0.0,0.0,1, emptyList(),1,0.0)
            , emptyList(), emptyList(),0)
        return weatherDetail
    }
}
