package com.example.skysiren.FavouritesFragment.FavouritesModelView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skysiren.DataBase.RoomState
import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.RepositoryInterface
import com.example.skysiren.Model.WeatherDetail
import com.example.skysiren.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouritViewModel(private val repo: RepositoryInterface): ViewModel() {
    private var weatherFromRoomMut : MutableStateFlow<RoomState> = MutableStateFlow<RoomState>(RoomState.Loading)
    val weatherFromRoom: StateFlow<RoomState> = weatherFromRoomMut

    fun getWeatherFromRoom(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getWeatherFromRoom()?.catch { e->
                weatherFromRoomMut.value=RoomState.Failure(e) }
                ?.collect{ data->
                    weatherFromRoomMut.value=RoomState.Success(data)
                }

        }
    }
    fun insertweatherToRoom(weatherDetail: FavouritWeather){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertWeatherToRoom(weatherDetail)
        }
    }
    fun deletweatheFromRoom(weatherDetail: FavouritWeather){
        viewModelScope.launch(Dispatchers.IO){
            repo.deletWeatherFromRoom(weatherDetail)
        }
    }

}