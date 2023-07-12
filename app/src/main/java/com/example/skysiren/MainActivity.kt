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

const val File_name: String = "PrefFile"

class MainActivity : AppCompatActivity() {
    lateinit var bindingSSc: ActivityMainBinding
    lateinit var pref: SharedPreferences

    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("TAG", "onCreate: splash screen ")
        bindingSSc = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingSSc.root)
        pref = getSharedPreferences("PrefFile", Context.MODE_PRIVATE)

        val loggedOrNot = pref.getBoolean("setUpComplete", false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Handler().postDelayed({
           // if (loggedOrNot) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
//            } else {
//                val intent = Intent(this, InitialSetupActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
        }, 1000)
    }
}