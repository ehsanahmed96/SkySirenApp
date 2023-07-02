package com.example.skysiren

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.skysiren.HomeActivity.HomeActivity
import com.example.skysiren.databinding.ActivityInitialSetupBinding

class InitialSetupActivity : AppCompatActivity() {
    lateinit var bindingISet: ActivityInitialSetupBinding
    var setUpComplete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingISet = ActivityInitialSetupBinding.inflate(layoutInflater)
        setContentView(bindingISet.root)
        Log.i("TAG", "onCreate:initial set up")
        val pref = getSharedPreferences("PrefFile", Context.MODE_PRIVATE)
        bindingISet.switchNoti.isChecked = true
        Log.i("TAG", "onCreate:initial set up after switch notifi ")

        bindingISet.btnSetUpDone.setOnClickListener {
            pref.edit().putBoolean("setUpComplete", true).apply()
            setUpComplete = true
            if (bindingISet.rbgps.isChecked) {
                pref.edit().putString("location", "gps").apply()
                Log.i("TAG", "onCreate:initial set up location is gps ")
            } else if (bindingISet.rbMap.isChecked) {
                pref.edit().putString("location", "map").apply()
            }

            bindingISet.switchNoti.setOnClickListener {
                if (bindingISet.switchNoti.isChecked) {
                    pref.edit().putBoolean("noti", true).apply()

                } else {
                    pref.edit().putBoolean("noti", false).apply()

                }
            }
            val locationPref = pref.getString("location", "")
            if (locationPref == "gps") {
                Log.i("TAG", "onCreate: $locationPref")
                val intent = Intent(this, HomeActivity::class.java)
                Log.i("TAG", "onCreate:  initial set up go to home activity")
                startActivity(intent)
                finish()
            } else if (locationPref == "map") {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                finish()
            }


        }
        Log.i("TAG", "onCreate:initial set up after set on click listener")

    }
}