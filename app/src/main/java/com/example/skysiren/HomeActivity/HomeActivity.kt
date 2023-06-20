package com.example.skysiren.HomeActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.skysiren.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//      var  actionBar : ActionBar = getSupportActionBar()

        bottomNavigationView = findViewById(R.id.NavBar)
        navController = findNavController(this, R.id.nav_host)
        setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnClickListener {
            if (it.id == R.id.homeFragment2) {
                navController.navigate(R.id.homeFragment2)
            }else if (it.id == R.id.alertFragment){
                navController.navigate(R.id.alertFragment)
            }else if(it.id == R.id.favouritesFragment){
                navController.navigate(R.id.favouritesFragment)
            } else if (it.id == R.id.settingFragment){
                navController.navigate(R.id.settingFragment)
            }
        }
    }
}