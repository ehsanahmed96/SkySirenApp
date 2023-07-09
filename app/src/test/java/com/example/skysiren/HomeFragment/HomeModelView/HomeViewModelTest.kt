package com.example.skysiren.HomeFragment.HomeModelView

import com.example.skysiren.Model.Current
import com.example.skysiren.Model.FakeRepository
import com.example.skysiren.Model.Weather
import com.example.skysiren.Model.WeatherDetail
import kotlinx.coroutines.flow.first
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runBlockingTest

class HomeViewModelTest{
    lateinit var repo:FakeRepository

    @Before
    fun setUp() {
        repo = FakeRepository()
    }
    @Test
    fun getWeatherFromRetrofit_locationInformation_setsWeatherState(){
        val viewModel :HomeViewModel
        viewModel = HomeViewModel(repo)
        viewModel.getWeatherFromRetrofit(31.5,32.2,"standard","en","958570d9d213a63daaf4a092ec70aa5b")
        assertThat(viewModel.weather.value, CoreMatchers.not(CoreMatchers.nullValue()))
    }

    @Test
    fun getOfflineWeather_setsWeatherOfflineState(){
        val viewModel :HomeViewModel
        viewModel = HomeViewModel(repo)
        viewModel.getOfflineWeather()
        assertThat(viewModel.weatherOffline.value, CoreMatchers.not(CoreMatchers.nullValue()))

    }
    @Test
    fun insertWeather_weatherDetails() = runBlockingTest{
        val viewModel = HomeViewModel(repo)
        val weatherDetail = WeatherDetail(
            lat = 31.5,
            lon = 32.2,
            timezone = "America/New_York",
            timezone_offset = -14400,
            alerts = listOf(),
            current = Current(
                clouds = 90,
                dew_point = 17.5,
                dt = 1625780185,
                feels_like = 25.2,
                humidity = 70,
                pressure = 1010,
                sunrise = 1625731723,
                sunset = 1625785123,
                temp = 22.3,
                uvi = 7.5,
                visibility = 10000,
                weather = listOf(
                    Weather(
                        description = "Clouds",
                        icon = "04d",
                        id = 804,
                        main = "Clouds"
                    )
                ),
                wind_deg = 220,
                wind_speed = 5.66
            ),
            daily = listOf(),
            hourly = listOf(),
            fav = 1
        )
        try {
            viewModel.insertWeather(weatherDetail)
            val returnWeatherList = repo.getOfflineweather().first()
            assertTrue(returnWeatherList.contains(weatherDetail))
        } catch (e: Exception) {
            println("Caught exception: ${e.message}")
            e.printStackTrace()
            fail("Test failed with exception: ${e.message}")
        }

    }
}