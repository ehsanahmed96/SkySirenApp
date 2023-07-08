package com.example.skysiren.AlertFragment.AlertViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skysiren.AlertFragment.AlertView.AlertState
import com.example.skysiren.DataBase.RoomState
import com.example.skysiren.HomeFragment.HomeView.OfflineWeatherState
import com.example.skysiren.Model.Alerts
import com.example.skysiren.Model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlertViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private var alertFromRoom: MutableStateFlow<AlertState> = MutableStateFlow<AlertState>(
        AlertState.Loading)
    val alertRoom: StateFlow<AlertState> = alertFromRoom

    private var weatherOfflineState : MutableStateFlow<OfflineWeatherState> = MutableStateFlow(OfflineWeatherState.Loading)
    val weatherOffline: StateFlow<OfflineWeatherState> = weatherOfflineState

    fun getAlertsfromRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllAlertsFromRoom()
                .catch { e ->
                    alertFromRoom.value = AlertState.Failure(e)
                }
                .collect { result ->
                    alertFromRoom.value = AlertState.Success(result)
                }
        }
    }


    fun insertAlertToRoom(alert:Alerts){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAlertToRoom(alert)
        }
    }

    fun deletAlertFromRoom(alert: Alerts){
        viewModelScope.launch (Dispatchers.IO){
            repo.deletAlertFromRoom(alert)
        }
    }
    fun getOfflineWeather(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getOfflineweather()
                ?.catch { e->
                    weatherOfflineState.value = OfflineWeatherState.Failure(e)
                }
                ?.collect{data->
                    weatherOfflineState.value= OfflineWeatherState.Success(data)
                    Log.i("TAG", "getOfflineWeather: get weather from room")
                }
        }
    }
}