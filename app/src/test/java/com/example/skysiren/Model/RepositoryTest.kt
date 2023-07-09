package com.example.skysiren.Model

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skysiren.DataBase.ConcreteLocalSource
import com.example.skysiren.DataBase.Localsource
import com.example.skysiren.Network.Api_Client
import com.example.skysiren.Network.RemoteSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = InstantTaskExecutorRule()
    val alert1 = Alerts(0L, 0L, 0L, 0L)
    val alert2 = Alerts(1L, 1L, 1L, 1L)
    val alert3 = Alerts(2L, 2L, 2L, 2L)

    val alertList = mutableListOf<Alerts>(alert1, alert2, alert3)

    val fav1 = FavouritWeather(30.2, 30.5)
    val fav2 = FavouritWeather(32.2, 32.5)
    val fav3 = FavouritWeather(34.2, 34.5)

    val favList = mutableListOf<FavouritWeather>(fav1, fav2, fav3)

    val weather1 = WeatherDetail(30.2, 30.5, "", 1231, emptyList(),
        Current(0, 0.0, 0, 0.0, 1, 1, 1,
            1, 0.0, 0.0, 1, emptyList(), 1, 0.0), emptyList(), emptyList(), 0)

    val weather2 = WeatherDetail(32.2, 32.5, "", 1231, emptyList(),
        Current(1, 1.0, 1, 1.0, 2, 2, 2,
            2, 1.0, 1.0, 2, emptyList(), 2, 1.0), emptyList(), emptyList(), 1)

    val weather3 = WeatherDetail(34.2, 34.5, "", 1231, emptyList(),
        Current(2, 2.0, 2, 2.0, 3, 3, 3,
            3, 2.0, 2.0, 3, emptyList(), 3, 2.0), emptyList(), emptyList(), 1)

    val weatherList = mutableListOf<WeatherDetail>(weather1, weather2, weather3)

    lateinit var fakeRemoteSource: FakeRemoteSource
    lateinit var fakeLocalSource: FakeLocalSourcee
    lateinit var repo: RepositoryInterface

    @Before
    fun setUp() {
        fakeRemoteSource = FakeRemoteSource(weather1)
        fakeLocalSource = FakeLocalSourcee(alertList, favList, weatherList)
        repo = Repository.getInstance(fakeRemoteSource, fakeLocalSource)

    }

    @Test
    fun getWeatherFromRetrofi_locationInformation_flowOfWeatherDetail() = runBlockingTest {
        repo.getWeatherFromRetrofit(30.2,
            50.4,
            "standard",
            "en",
            "958570d9d213a63daaf4a092ec70aa5b")?.collect() {
            val weather = it

            assertThat(weather, IsEqual(weather1))
        }

    }

    @Test
    fun getWeatherFromroom_flowOfListOfFavouriteWeather() = runBlockingTest {
        repo.getWeatherFromRoom().collect() {
    val favWeatherListReturn = it
            assertThat(favWeatherListReturn, IsEqual(favList))
        }
    }
    @Test
    fun getOfflineWeatherFromroom_flowOfListOfweatherDetail() = runBlockingTest {
        repo.getOfflineweather().collect() {
            val weatherListReturn = it
            assertThat(weatherListReturn, IsEqual(weatherList))
        }
    }

    @Test
    fun getAllAlertsFromRoom__flowOfListOfAlerts() = runBlockingTest {
        repo.getAllAlertsFromRoom().collect(){
            val alertListreturn = it
            assertThat(alertListreturn, IsEqual(alertList))
        }

    }

    @Test
    fun insertWeather_weatherDetailObj() = runBlockingTest {
        val weather4 = WeatherDetail(34.2, 34.5, "", 1231, emptyList(),
            Current(2, 2.0, 2, 2.0, 3, 3, 3,
                3, 2.0, 2.0, 3, emptyList(), 3, 2.0), emptyList(), emptyList(), 1)
        repo.insertWeather(weather4)
        val result = repo.getOfflineweather().first()
        assertTrue(result.contains(weather4))
    }
    @Test
    fun insertFavWeather_FavouriteweatherDetailObj() = runBlockingTest {
        val fav4 = FavouritWeather(34.2, 34.5)
        repo.insertWeatherToRoom(fav4)
        val result = repo.getWeatherFromRoom().first()
        assertTrue(result.contains(fav4))
    }

    @Test
    fun insertAlertWeather_AlertsObj() = runBlockingTest {
        val alert4 = Alerts(2L, 2L, 2L, 2L)
        repo.insertAlertToRoom(alert4)
        val result = repo.getAllAlertsFromRoom().first()
        assertTrue(result.contains(alert4))
    }

    @Test
    fun deleteFavWeatherFromRoom () = runBlockingTest {
        repo.deletWeatherFromRoom(fav2)
        val returnFavList = repo.getWeatherFromRoom().first()
        assertFalse(returnFavList.contains(fav2))

    }

    @Test
    fun deleteAlertFromRoom () = runBlockingTest {
        repo.deletAlertFromRoom(alert2)
        val returnFavList = repo.getAllAlertsFromRoom().first()
        assertFalse(returnFavList.contains(alert2))

    }
}