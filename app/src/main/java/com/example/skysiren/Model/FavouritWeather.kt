package com.example.skysiren.Model

import androidx.room.Entity


@Entity(tableName="favourite",primaryKeys = ["latitude", "longitude"])
data class FavouritWeather(
                           val latitude:Double,
                           val longitude:Double)
