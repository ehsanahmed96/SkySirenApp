package com.example.skysiren.Model

import androidx.room.Entity


@Entity(tableName = "Alerts",primaryKeys = ["startDate", "endDate"])
data class Alerts(var startDate: Long, var endDate: Long, var startimeOfAlert: Long ,var endTimeOfAlert : Long , var isAlert:String)
