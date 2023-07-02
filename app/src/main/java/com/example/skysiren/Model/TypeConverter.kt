package com.example.skysiren.Model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

 class TypeConverter {
    @TypeConverter
    fun fromStringToAlert(value: String?): List<Alert>? {
        if (value == null || value.isEmpty()) {
            return null
        }
        val listType = object : TypeToken<List<Alert>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListToString(list: List<Alert>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
     @TypeConverter
     fun fromCurrentToString(current:Current) = Gson().toJson(current)
     @TypeConverter
     fun fromStringToCurrent(currentString : String) = Gson().fromJson(currentString,Current::class.java)

     @TypeConverter
     fun fromHourlyToString(hourly: List<Hourly>) = Gson().toJson(hourly)
     @TypeConverter
     fun fromStringToHourly(hourlyString : String) = Gson().fromJson(hourlyString, Array<Hourly>::class.java).toList()

     @TypeConverter
     fun fromDailyToString(daily: List<Daily>) = Gson().toJson(daily)
     @TypeConverter
     fun fromStringToDaily(dailyString : String) = Gson().fromJson(dailyString, Array<Daily>::class.java).toList()

     @TypeConverter
     fun fromTempToString(temp: Temp) = Gson().toJson(temp)
     @TypeConverter
     fun fromStringToTemp(tempString : String) = Gson().fromJson(tempString,Temp::class.java)

     @TypeConverter
     fun fromFeelsLikeToString(feelsLike:FeelsLike) = Gson().toJson(feelsLike)
     @TypeConverter
     fun fromStringToFeelsLike(feelLiksString : String) = Gson().fromJson(feelLiksString,FeelsLike::class.java)

     @TypeConverter
     fun fromWeatherToString(weather: List<Weather>) = Gson().toJson(weather)
     @TypeConverter
     fun fromStringToWeather(weatherString : String) = Gson().fromJson(weatherString, Array<Weather>::class.java).toList()
 }