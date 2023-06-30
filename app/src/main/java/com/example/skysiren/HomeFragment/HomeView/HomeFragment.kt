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
import com.example.skysiren.databinding.FragmentHomeBinding
import com.google.android.gms.location.*
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*
import java.text.NumberFormat


const val api_key = "958570d9d213a63daaf4a092ec70aa5b"

class HomeFragment : Fragment() {

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
        // Inflate the layout for this fragment
        pref = requireActivity().getSharedPreferences("PrefFile", Context.MODE_PRIVATE)
        editor = pref.edit()

        latitude = pref.getString("lat", null)?.toDoubleOrNull() ?: 0.0
        Log.i("TAG", "onViewCreated: $latitude")
        longitude = pref.getString("lon", null)?.toDoubleOrNull() ?: 0.0
        Log.i("TAG", "onViewCreated: $longitude")
        bindingHF = FragmentHomeBinding.inflate(inflater, container, false)

        return bindingHF.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var lang = "en"
        var unit = "metric"
        val measureUnit = "m/s"


        homeFactory = HomeViewModelFactory(Repository.getInstance(Api_Client()/*,
            ConcreteLocalSource.getInstance(requireContext())*/))
        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)


        if (isNetworkAvailable(requireContext())) {
            getRemoteWeather(latitude, longitude, lang, unit)
            lifecycleScope.launch {
                viewModel.weather.collect { result ->
                    when (result) {
                        is ApiState.Loading -> {
                            bindingHF.progressBar.visibility = View.VISIBLE
                            bindingHF.scrollView2.visibility = View.GONE

                        }
                        is ApiState.Success -> {
                            bindingHF.progressBar.visibility = View.GONE
                            bindingHF.scrollView2.visibility = View.VISIBLE
                            display(result.weather, unit, measureUnit, lang)


                        }
                        else -> {


                        }
                    }


                }
            }
        } else {
            Toast.makeText(requireContext(), "chech youre internet", Toast.LENGTH_SHORT).show()
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
    fun display(
        result: WeatherDetail,
        unit: String,
        measurement: String,
        lang: String,
    ) {
        val formatter = NumberFormat.getInstance(Locale(lang))
        val geocoder = Geocoder(requireContext() , Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(result.lat, result.lon, 1) as List<Address>

        HourlyAdapter =
            HourlyAdapter(result, requireContext(), unit, lang)
        bindingHF.hourRv.adapter = HourlyAdapter

        DailyAdapter =
            DailyAdapter(result, requireContext(), unit, lang)
        bindingHF.dayRv.adapter = DailyAdapter
        bindingHF.descTxt.text = result.current.weather[0].description
        Icons.replaceIcons(result.current.weather.get(0).icon , bindingHF.iconWeather)

        if(addresses .isNotEmpty()) {
            bindingHF.LocationName.text ="${addresses[0].locality}/${addresses[0].countryName}"
        }
        bindingHF.dateTxt.text = date(lang)

        val formattedNumber = formatter.format(result.current.temp)
        bindingHF.tempTxt.text = "${formattedNumber}°k"

        bindingHF.valueWind.text =
            result.current.wind_speed.toString()
        bindingHF.valuePressure.text =
            "${result.current.pressure} hpa"
        bindingHF.valueHumidity.text =
            "${result.current.humidity} %"
        bindingHF.valueCloud.text =
            "${result.current.clouds} %"
        bindingHF.valueUltraViolet.text =
            result.current.uvi.toString()
        bindingHF.valueVisibility.text =
            "${result.current.visibility} m"
//        when (unit) {
//            "metric" -> {
//                val formattedNumber = formatter.format(weatherDetail.current.temp)
//                binding.txtTemp.text = "${formattedNumber}°C"
//                if (measurement.equals("m/s")) {
//                    binding.txtWind.text = "${weatherDetail.current.wind_speed} m/s"
//                } else {
//                    binding.txtWind.text =
//                        "${convertWindSpeedMpH(weatherDetail.current.wind_speed).toInt()} m/h"
//                }
//            }
//            "imperial" -> {
//                val formattedNumber = formatter.format(weatherDetail.current.temp)
//                binding.txtTemp.text = "${formattedNumber}°f"
//                if (measurement.equals("m/s")) {
//                    binding.txtWind.text =
//                        "${convertWindSpeedMpS(weatherDetail.current.wind_speed).toInt()} m/s"
//                } else {
//                    binding.txtWind.text = "${weatherDetail.current.wind_speed} m/h"
//                }
//            }
//            else -> {
//                val formattedNumber = formatter.format(weatherDetail.current.temp)
//                binding.txtTemp.text = "${formattedNumber}°k"
//                if (measurement.equals("m/s")) {
//                    binding.txtWind.text = "${weatherDetail.current.wind_speed} m/s"
//                } else {
//                    binding.txtWind.text =
//                        "${convertWindSpeedMpH(weatherDetail.current.wind_speed).toInt()} m/h"
//                }
//            }
//        }


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