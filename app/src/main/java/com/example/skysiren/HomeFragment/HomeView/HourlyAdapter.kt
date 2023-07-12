package com.example.skysiren.HomeFragment.HomeView

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skysiren.Model.Hourly
import com.example.skysiren.Model.Icons
import com.example.skysiren.Model.WeatherDetail
import com.example.skysiren.R
import com.example.skysiren.databinding.HourRowItemBinding
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class HourlyAdapter(
    var weather: WeatherDetail,
    val context: Context,
    val unit:String,val lang:String
) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    private lateinit var binding: HourRowItemBinding

    val number = NumberFormat.getInstance(Locale(lang))

    inner class ViewHolder(var binding: HourRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = HourRowItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return weather.hourly.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (weather != null) {

            var HourList: Hourly = weather.hourly[position]
            Icons.replaceIcons(HourList.weather.get(0).icon , holder.binding.imagHourRV)
            holder.binding.hourTxt.text = getZoneTime(HourList.dt, weather.timezone)
            val tempFormatted = number.format(HourList.temp.toInt())
            when (unit) {
                "metric" -> {

                    holder.binding.tempTxt.text = "${tempFormatted}°C"
                }
                "imperial" -> {

                    holder.binding.tempTxt.text = "${tempFormatted}°F"
                }
                else -> {

                    holder.binding.tempTxt.text = "${tempFormatted}°K"
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getZoneTime(dt:Int, timeZone: String): String {
        val instant = Instant.ofEpochSecond(dt.toLong())
        val zoneId = ZoneId.of(timeZone)
        val zonedDateTime = instant.atZone(zoneId)
        val formatter = DateTimeFormatter.ofPattern("K:mm a",Locale(lang))
        return formatter.format(zonedDateTime)
    }

}