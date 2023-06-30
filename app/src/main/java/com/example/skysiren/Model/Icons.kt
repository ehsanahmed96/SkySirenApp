package com.example.skysiren.Model

import android.widget.ImageView
import com.example.skysiren.R

object Icons {
    const val ICON: String = "icon"

    fun replaceIcons(image : String , imageView :ImageView){
        when(image){
            "01d" -> imageView.setImageResource(R.drawable.clear_sky_day)
            "01n" -> imageView.setImageResource(R.drawable.clear_sky_night)

            "02d" -> imageView.setImageResource(R.drawable.few_clouds_day)
            "02n" -> imageView.setImageResource(R.drawable.few_clouds_night)

            "03d" -> imageView.setImageResource(R.drawable.scatterd_cloud_day)
            "03n" -> imageView.setImageResource(R.drawable.scattered_cloud_night)

            "04d" -> imageView.setImageResource(R.drawable.broken_cloud_dayandnight)
            "04n" -> imageView.setImageResource(R.drawable.broken_cloud_dayandnight)

            "09d" -> imageView.setImageResource(R.drawable.showe_rain_dayandnight)
            "09n" -> imageView.setImageResource(R.drawable.showe_rain_dayandnight)

            "10d" -> imageView.setImageResource(R.drawable.rain_day)
            "10n" -> imageView.setImageResource(R.drawable.rain_night)

            "11d" -> imageView.setImageResource(R.drawable.thunder_dayandnight)
            "11n" -> imageView.setImageResource(R.drawable.thunder_dayandnight)

            "13d" -> imageView.setImageResource(R.drawable.snow_dayandnight)
            "13n" -> imageView.setImageResource(R.drawable.snow_dayandnight)

            "50d" -> imageView.setImageResource(R.drawable.mist_dayandnight)
            "50n" -> imageView.setImageResource(R.drawable.mist_dayandnight)

        }
    }
}