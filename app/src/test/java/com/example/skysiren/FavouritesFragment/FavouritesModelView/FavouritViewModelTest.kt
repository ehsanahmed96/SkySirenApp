package com.example.skysiren.FavouritesFragment.FavouritesModelView

import com.example.skysiren.HomeFragment.HomeModelView.HomeViewModel
import com.example.skysiren.Model.FakeRepository
import com.example.skysiren.Model.FavouritWeather
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavouritViewModelTest{
    lateinit var repo: FakeRepository

    @Before
    fun setUp() {
        repo = FakeRepository()
    }

    @Test
    fun getWeatherFromRoom_setsWeatherState() = runBlockingTest{
        val viewModel : HomeViewModel
        viewModel = HomeViewModel(repo)
        viewModel.getOfflineWeather()
        assertThat(viewModel.weather.value, CoreMatchers.not(CoreMatchers.nullValue()))
    }

    @Test
    fun insertweatherToRoom_FavouriteWeather() = runBlockingTest{
        val viewModel = FavouritViewModel(repo)
        val favWeather = FavouritWeather(30.2,31.5)
        viewModel.insertweatherToRoom(favWeather)
        val retunFavWeather = repo.getWeatherFromRoom().first()
        assertTrue(retunFavWeather.contains(favWeather))

    }

    @Test
    fun deletWeatherFromroom_FavouriteWeather() = runBlockingTest {
        val viewModel = FavouritViewModel(repo)
        val favWeather = FavouritWeather(30.2,31.5)
        viewModel.deletweatheFromRoom(favWeather)
        val retunDeletedFavWeather = repo.getWeatherFromRoom().first()
        assertFalse(retunDeletedFavWeather.contains(favWeather))
    }
}