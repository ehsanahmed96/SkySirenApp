package com.example.skysiren.HomeFragment.HomeView

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skysiren.DataBase.ConcreteLocalSource
import com.example.skysiren.File_name
import com.example.skysiren.HomeActivity.PERMISSION_ID
import com.example.skysiren.HomeFragment.HomeModelView.HomeViewModel
import com.example.skysiren.HomeFragment.HomeModelView.HomeViewModelFactory
import com.example.skysiren.Model.Daily
import com.example.skysiren.Model.Icons
import com.example.skysiren.Model.Repository
import com.example.skysiren.Model.WeatherDetail
import com.example.skysiren.Network.ApiState
import com.example.skysiren.Network.Api_Client
import com.example.skysiren.R
import com.example.skysiren.SettingFragment.SettingView.SettingFragment
import com.example.skysiren.SettingFragment.SettingView.SettingInterface
import com.example.skysiren.databinding.FragmentHomeBinding
import com.google.android.gms.location.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*
import java.text.NumberFormat


const val api_key = "958570d9d213a63daaf4a092ec70aa5b"

class HomeFragment : Fragment(){
    lateinit var bindingHF: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var homeFactory: HomeViewModelFactory
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    lateinit var HourlyAdapter: HourlyAdapter
    lateinit var DailyAdapter: DailyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        pref = requireActivity().getSharedPreferences("PrefFile", Context.MODE_PRIVATE)
        editor = pref.edit()

        if (pref.getString("location", "gps").equals("gps")){

            latitude = pref.getString("lat", null)?.toDoubleOrNull() ?: 0.0
            longitude = pref.getString("lon", null)?.toDoubleOrNull() ?: 0.0

        }else if(pref.getString("location", "map").equals("map")){

            latitude = pref.getString("latMap", null)?.toDoubleOrNull() ?: 0.0
            longitude = pref.getString("lonMap", null)?.toDoubleOrNull() ?: 0.0
        }


        bindingHF = FragmentHomeBinding.inflate(inflater, container, false)

        return bindingHF.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editor.putString("flag", "home").apply()
        var language = pref.getString("lang" ,"non").toString()
        var unitTemp = pref.getString("temp" , "standard").toString()
        var measureUnit = pref.getString("measureUnit","m/s").toString()





        homeFactory = HomeViewModelFactory(Repository.getInstance(Api_Client(),
            ConcreteLocalSource.getInstance(requireContext())))
        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)


        if (isNetworkAvailable(requireContext())) {
            getRemoteWeather(latitude, longitude, language, unitTemp)
            lifecycleScope.launch {
                viewModel.weather.collect { result ->
                    when (result) {
                        is ApiState.Loading -> {
                            bindingHF.progressBar.visibility = View.VISIBLE
                            bindingHF.scrollView2.visibility = View.GONE

                        }
                        is ApiState.Success -> {
                            viewModel.insertWeather(result.weather)
                            Log.i("TAG", "onViewCreated: insert weather to room")
                            bindingHF.progressBar.visibility = View.GONE
                            bindingHF.scrollView2.visibility = View.VISIBLE
                            display(result.weather, language, measureUnit, unitTemp)


                        }
                        else -> {


                        }
                    }

                }
            }
        } else {
            Toast.makeText(requireContext(), "chech your internet \n this is not Updating data ", Toast.LENGTH_LONG).show()
            Log.i("TAG", "onViewCreated: offline mode")
            viewModel.getOfflineWeather()
            lifecycleScope.launch {
                viewModel.weatherOffline.collectLatest{result ->
                    when(result){
                        is OfflineWeatherState.Loading -> {
                            bindingHF.progressBar.visibility = View.VISIBLE
                            bindingHF.scrollView2.visibility = View.GONE
                            Log.i("TAG", "onViewCreated: loading")

                        }
                        is OfflineWeatherState.Success -> {
                            bindingHF.progressBar.visibility = View.GONE
                            bindingHF.scrollView2.visibility = View.VISIBLE
                            if (result.weather != null && result.weather.size > 0){
                                 val weather =result.weather?.last()
                                    ?.let {  WeatherDetail(
                                        it.lat,
                                        it.lon,
                                        it.timezone,
                                        it.timezone_offset,
                                        it.alerts,
                                        it.current,
                                        it.daily,
                                        it.hourly,
                                    )
                                      }
                                if (weather != null) {
                                    display(weather, language, measureUnit, unitTemp)
                                }
                            }else
                                Log.i("TAG", "onViewCreated: empty data")


                        }
                        else -> {



                        }
                    }

                }
            }


        }

    }

    companion object {

    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    fun getRemoteWeather(lat: Double?, lon: Double?, lang: String?, unit: String?) {

        if (lat != null) {
            if (lon != null) {
                if (lang != null) {
                    if (unit != null) {
                        viewModel.getWeatherFromRetrofit(
                            lat, lon, unit, lang, api_key
                        )
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun display(result: WeatherDetail, lang: String, measureUnit: String, unit: String) {
        val numFormatter = NumberFormat.getInstance(Locale(lang))
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(result.lat, result.lon, 1) as List<Address>

        HourlyAdapter = HourlyAdapter(result, requireContext(), unit, lang)
        bindingHF.hourRv.adapter = HourlyAdapter

        DailyAdapter = DailyAdapter(result, requireContext(), unit, lang)
        bindingHF.dayRv.adapter = DailyAdapter

        bindingHF.descTxt.text = result.current.weather[0].description
        Icons.replaceIcons(result.current.weather.get(0).icon, bindingHF.iconWeather)

        if (addresses.isNotEmpty()) {
            Log.i("TAG", "getFullAddress: ${addresses[0].adminArea}")
            bindingHF.LocationName.text = "${addresses[0].adminArea}/${addresses[0].countryName}"
        }
        bindingHF.dateTxt.text = date(lang)

        bindingHF.valueWind.text = result.current.wind_speed.toString()

        bindingHF.valuePressure.text = "${result.current.pressure} hpa"

        bindingHF.valueHumidity.text = "${result.current.humidity} %"

        bindingHF.valueCloud.text = "${result.current.clouds} %"

        bindingHF.valueUltraViolet.text = result.current.uvi.toString()

        bindingHF.valueVisibility.text = "${result.current.visibility} m"

        val formattedNumber = numFormatter.format(result.current.temp)

        when (unit) {
            "metric" -> {

                bindingHF.tempTxt.text = "${formattedNumber}°C"
                if (measureUnit.equals("m/s")) {
                    bindingHF.valueWind.text = "${result.current.wind_speed} m/s"
                } else {
                    bindingHF.valueWind.text =
                        "${convertMilesPerHourToMetersPerSecond(result.current.wind_speed).toInt()} m/h"
                }
            }
            "imperial" -> {

                bindingHF.tempTxt.text = "${formattedNumber}°f"
                if (measureUnit.equals("m/s")) {
                    bindingHF.valueWind.text =
                        "${convertMetersPerSecondToMilesPerHour(result.current.wind_speed).toInt()} m/s"
                } else {
                    bindingHF.valueWind.text = "${result.current.wind_speed} m/h"
                }
            }
            else -> {

                bindingHF.tempTxt.text = "${formattedNumber}°k"
                if (measureUnit.equals("m/s")) {
                    bindingHF.valueWind.text = "${result.current.wind_speed} m/s"
                } else {
                    bindingHF.valueWind.text =
                        "${convertMilesPerHourToMetersPerSecond(result.current.wind_speed).toInt()} m/h"
                }
            }
        }


    }

    fun convertMetersPerSecondToMilesPerHour(mps: Double): Double {
        return mps * 2.23694

    }

    fun convertMilesPerHourToMetersPerSecond(mph: Double): Double {
        return mph / 2.23694

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun date(lang: String): String? {
        val currentDateTime = LocalDateTime.now()
        val locale = Locale(lang)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(locale)
            .withDecimalStyle(DecimalStyle.of(locale))
        val formattedDateTime = currentDateTime.format(formatter)
        return formattedDateTime
    }



}