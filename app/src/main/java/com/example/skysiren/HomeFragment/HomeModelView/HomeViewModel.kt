package com.example.skysiren.HomeFragment.HomeModelView

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skysiren.HomeFragment.HomeView.OfflineWeatherState
import com.example.skysiren.Model.RepositoryInterface
import com.example.skysiren.Model.WeatherDetail
import com.example.skysiren.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepositoryInterface): ViewModel()  {
    private var weatherState : MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val weather: StateFlow<ApiState> = weatherState

    private var weatherOfflineState : MutableStateFlow<OfflineWeatherState> = MutableStateFlow(OfflineWeatherState.Loading)
    val weatherOffline: StateFlow<OfflineWeatherState> = weatherOfflineState

    fun getWeatherFromRetrofit(lat:Double?,lon:Double?,units:String,lang:String,apiKey:String)
    {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getWeatherFromRetrofit(lat,lon,units,lang, apiKey)
            ?.catch { e->
                weatherState.value=ApiState.Failure(e) }
            ?.collect{ data->
                weatherState.value=ApiState.Success(data)
            }
        }
    }


    fun getOfflineWeather(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getOfflineweather()
                ?.catch { e->
                    weatherOfflineState.value = OfflineWeatherState.Failure(e)
                }
                ?.collect{data->
                    weatherOfflineState.value=OfflineWeatherState.Success(data)
                    Log.i("TAG", "getOfflineWeather: get weather from room")
                }
        }
    }

    fun insertWeather(weatherDetail: WeatherDetail){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertWeather(weatherDetail)
            Log.i("TAG", "insertWeather: view model")
        }
    }
}