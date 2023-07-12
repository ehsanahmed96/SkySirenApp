package com.example.skysiren


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.skysiren.HomeActivity.HomeActivity
import com.example.skysiren.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    lateinit var gMap: GoogleMap
    lateinit var bindingMap: ActivityMapBinding
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var searchEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMap = ActivityMapBinding.inflate(layoutInflater)
        setContentView(bindingMap.root)
        pref = getSharedPreferences(File_name, Context.MODE_PRIVATE)
        editor = pref.edit()

        val map = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        map.getMapAsync(this)
        searchEditText=bindingMap.searchView

        bindingMap.buttonSearch.setOnClickListener {
            val locationName = searchEditText.text.toString()
            if (locationName.isNotEmpty()) {
                searchLocation(locationName)
            } else {
                Toast.makeText(this, "Please enter a location to search", Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        var Location = LatLng(-34.0, 151.0)
        gMap = googleMap

        gMap.setOnMapClickListener(GoogleMap.OnMapClickListener {
            Location = LatLng(it.latitude, it.longitude)
            pref.edit().putString("location", "map").apply()
            pref.edit().putString("latMap", it.latitude.toString()).apply()//
            pref.edit().putString("lonMap", it.longitude.toString()).apply()//
            gMap.addMarker(MarkerOptions().position(Location).title("location"))
            gMap.moveCamera(CameraUpdateFactory.newLatLng(Location))
            confirm(Location)
        })
    }

    private fun confirm(latLng: LatLng) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Location Confirm")
        builder.setMessage(" " + getFullAddress(latLng.latitude, latLng.longitude) + " ?")
        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            if (pref.getString("flag", "none").equals("fav")) {
                val Intent = Intent()
                Intent.putExtra("lat", latLng.latitude.toString())
                Intent.putExtra("lon", latLng.longitude.toString())

                Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, Intent)
                finish()
            } else if (pref.getString("flag", "none").equals("home")) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else if (pref.getString("flag", "none").equals("setting")) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, which -> }
        builder.show()
    }

    private fun getFullAddress(latitude: Double, longitude: Double): Any {
        var geocoder = Geocoder(this)
        var allAddress = ""
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                var city = addresses[0].adminArea
                if (city == null) {
                    Toast.makeText(this, "choose more specefic area", Toast.LENGTH_SHORT).show()
                } else {
                    Log.i("TAG", "getFullAddress city: $city")
                    var country = addresses[0].countryName
                    Log.i("TAG", "getFullAddress country: $country")
                    allAddress = "$city,$country"
                }
            }
        } catch (e: IOException) {
            Log.e("TAG", "getFullAddress: ${e.message}")
        }
        return allAddress
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        val latLng = marker.position
        val latitude = latLng.latitude
        val longitude = latLng.longitude
        Toast.makeText(this,
            "marker click at : lat = $latitude, lon = $longitude",
            Toast.LENGTH_SHORT).show()
        return true
    }

    private fun searchLocation(locationName: String) {
        try {
            val geocoder = Geocoder(this)

            val addresses: List<Address> = geocoder.getFromLocationName(locationName, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                val latLng = LatLng(address.latitude, address.longitude)
                addMarkerToMap(latLng)
                searchEditText.setText("")
                pref.edit().putString("location", "map").apply()
                pref.edit().putString("latMap", latLng.latitude.toString()).apply()
                pref.edit().putString("lonMap", latLng.longitude.toString()).apply()
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            Log.e("TAG", "searchLocation: ${e.message}")
            Toast.makeText(this, "Error occurred during location search", Toast.LENGTH_SHORT).show()
        }
    }
    private fun addMarkerToMap(latLng: LatLng) {
        gMap.clear() // Clear existing markers
        gMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        confirm(latLng)
    }
}