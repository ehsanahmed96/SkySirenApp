package com.example.skysiren

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.skysiren.HomeActivity.HomeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({

            intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()

        }, 5000)
    }
}