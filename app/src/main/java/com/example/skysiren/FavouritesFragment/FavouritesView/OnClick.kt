package com.example.skysiren.FavouritesFragment.FavouritesView

import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.WeatherDetail

interface OnClick {
    fun onClick(favModel: FavouritWeather)
    fun onDeleteIcon(favModel: FavouritWeather)
}