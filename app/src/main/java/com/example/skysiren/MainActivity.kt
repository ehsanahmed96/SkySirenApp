package com.example.skysiren

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.LottieAnimationView
import com.example.skysiren.HomeActivity.HomeActivity
import com.example.skysiren.databinding.ActivityMainBinding

const val File_name :String ="PrefFile"
class MainActivity : AppCompatActivity() {
lateinit var bindingSSc : ActivityMainBinding
lateinit var pref :SharedPreferences

    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSSc =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingSSc.root)
        pref =getSharedPreferences("PrefFile",Context.MODE_PRIVATE)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}