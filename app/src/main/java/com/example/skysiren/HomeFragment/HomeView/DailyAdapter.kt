package com.example.skysiren.HomeFragment.HomeView

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skysiren.Model.Daily
import com.example.skysiren.Model.Icons
import com.example.skysiren.Model.WeatherDetail
import com.example.skysiren.databinding.DayRowItemBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(var weather: WeatherDetail, val context: Context, val unit:String, val lang:String)
    : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    private lateinit var binding: DayRowItemBinding
    val number = NumberFormat.getInstance(Locale(lang))

    inner class ViewHolder(var binding: DayRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DayRowItemBinding.inflate(inflater, parent, false)
        Log.i(ContentValues.TAG, "onCreateViewHolder: ")
        return ViewHolder(binding)
    }

    fun setList(list: WeatherDetail) {
        weather = list
    }

    override fun getItemCount(): Int {
        return weather.daily.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (weather != null) {
            var Day: Daily = weather.daily[position]
           Icons.replaceIcons(Day.weather.get(0).icon , holder.binding.rvImgWDay)
            holder.binding.txtDayName.text = getDay(Day.dt,lang)
            holder.binding.rvTxtTemp.text = Day.weather.get(0).description
            val tempFormattedMin = number.format(Day.temp.min.toInt())
            val tempFormattedMax = number.format(Day.temp.max.toInt())
            when (unit) {
                "metric" -> {

                    holder.binding.rvNumTemp.text = "${tempFormattedMin}/${tempFormattedMax}°C"
                }
                "imperial" -> {

                    holder.binding.rvNumTemp.text = "${tempFormattedMin}/${tempFormattedMax}°F"
                }
                else -> {

                    holder.binding.rvNumTemp.text = "${tempFormattedMin}/${tempFormattedMax}°K"
                }
            }


        }
    }

    fun getDay(dt: Int,lang:String): String {
        val format = SimpleDateFormat("E",Locale(lang))
        val date = Date(dt.toLong() * 1000)
        return format.format(date)
    }

}