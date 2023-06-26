package com.example.skysiren.HomeFragment.HomeView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skysiren.DataBase.ConcreteLocalSource
import com.example.skysiren.HomeFragment.HomeModelView.HomeViewModel
import com.example.skysiren.HomeFragment.HomeModelView.HomeViewModelFactory
import com.example.skysiren.Model.Repository
import com.example.skysiren.Model.WeatherDetail
import com.example.skysiren.Network.ApiState
import com.example.skysiren.Network.Api_Client
import com.example.skysiren.R
import com.google.gson.Gson
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    lateinit var viewModel: HomeViewModel
    lateinit var homeFactory: HomeViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lat = 37.7749 // Replace with your desired latitude
        val lon = -122.4194 // Replace with your desired longitude
        var lang = "en"
        var unit = "metric"
        val exclude = "" // Replace with your desired exclude values
        val apiKey = "958570d9d213a63daaf4a092ec70aa5b"
        homeFactory = HomeViewModelFactory(Repository.getInstance(Api_Client()/*,
            ConcreteLocalSource.getInstance(requireContext())*/))

        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)
        viewModel.getWeatherFromRetrofit(lat, lon, lang, unit, apiKey)


        lifecycleScope.launch {
            viewModel.weather.collect { result ->
                //Log.i("TAG", "onCreate: collect latest")
                when (result) {
                    is ApiState.Loading -> {

                    }
                    is ApiState.Success -> {
                        val gson = Gson()
                        //val weatherDetail = gson.fromJson(result, WeatherDetail::class.java)
                        //val hourlyList = weatherDetail.hourly
                        Log.i("TAG", "onViewCreateeeeeeeeeeed:${result.weather.hourly} ")

                    }
                    else -> {


                    }
                }


            }
        }
    }

    companion object {

    }
}