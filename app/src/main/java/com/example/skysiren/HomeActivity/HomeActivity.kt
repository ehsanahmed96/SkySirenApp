package com.example.skysiren.HomeActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.skysiren.InitialSetupActivity
import com.example.skysiren.MapActivity
import com.example.skysiren.R
import com.example.skysiren.databinding.ActivityHomeBinding
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


const val PERMISSION_ID = 44

class HomeActivity : AppCompatActivity() {
    var lat: Double = 0.0
    var lon: Double = 0.0

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var pref: SharedPreferences
    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var bindingHA: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        bindingHA = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindingHA.root)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        pref = getSharedPreferences("PrefFile", Context.MODE_PRIVATE)
        val loggedOrNot = pref.getBoolean("setUpComplete", false)


        if (!loggedOrNot) {
            val intent = Intent(this, InitialSetupActivity::class.java)
            Log.i("TAG", "onCreate: home activity go to initial set up")
            startActivity(intent)
            finish()
        } else {
            val gpsLocation = pref.getString("location", "gps")
            if (gpsLocation.equals("gps")) {
                getLastLocation()
            }
            Log.i("TAG", "onCreate: home activity  elsssssssssssse")

        }
        bottomNavigationView = findViewById(R.id.NavBar)
        navController = findNavController(this, R.id.nav_host)
        setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnClickListener {
            if (it.id == R.id.homeFragment2) {
                navController.navigate(R.id.homeFragment2)
            } else if (it.id == R.id.alertFragment) {
                navController.navigate(R.id.alertFragment)
            } else if (it.id == R.id.favouritesFragment) {
                navController.navigate(R.id.favouritesFragment)
            } else if (it.id == R.id.settingFragment) {
                navController.navigate(R.id.settingFragment)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.location_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map_icon -> {
                startActivity(Intent(this, MapActivity::class.java))
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (pref.getString("location", "gps").equals("gps")) {
            if (checkPermissions())
                getLastLocation()
        }

    }


    private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return result
    }

    @SuppressLint("MissingPermission")
    public fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            PERMISSION_ID
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(0)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            if (mLastLocation != null) {
                lat = mLastLocation.latitude
                pref.edit().putString("lat", lat.toString()).apply()
                lon = mLastLocation.longitude
                pref.edit().putString("lon", lon.toString()).apply()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

}