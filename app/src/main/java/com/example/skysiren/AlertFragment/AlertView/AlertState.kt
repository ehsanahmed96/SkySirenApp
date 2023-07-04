package com.example.skysiren.AlertFragment.AlertView


import com.example.skysiren.Model.Alerts


sealed class AlertState {
    class Success(val alert: List<Alerts>) : AlertState()
    class Failure(val msg: Throwable) : AlertState()
    object Loading : AlertState()
}