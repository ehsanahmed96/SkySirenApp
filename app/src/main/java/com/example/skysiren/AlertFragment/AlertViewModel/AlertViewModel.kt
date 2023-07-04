package com.example.skysiren.AlertFragment.AlertViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skysiren.AlertFragment.AlertView.AlertState
import com.example.skysiren.DataBase.RoomState
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
}