package com.example.skysiren.FavouritesFragment.FavouritesView

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skysiren.DataBase.ConcreteLocalSource
import com.example.skysiren.DataBase.RoomState
import com.example.skysiren.FavouritesFragment.FavouritesModelView.FavouritViewModel
import com.example.skysiren.FavouritesFragment.FavouritesModelView.FavouritViewModelFactory
import com.example.skysiren.File_name
import com.example.skysiren.HomeActivity.HomeActivity
import com.example.skysiren.MapActivity
import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.Repository
import com.example.skysiren.Network.ApiState
import com.example.skysiren.Network.Api_Client
import com.example.skysiren.R
import com.example.skysiren.databinding.FragmentFavouritesBinding
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment(), OnClick {
    lateinit var bindingFF: FragmentFavouritesBinding
    lateinit var adapterFA: FavouirteAdapter
    lateinit var viewModel: FavouritViewModel
    lateinit var favouriteFactory: FavouritViewModelFactory
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingFF = FragmentFavouritesBinding.inflate(inflater, container, false)
        return bindingFF.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireActivity().getSharedPreferences(File_name, Context.MODE_PRIVATE)
        editor = pref.edit()
        adapterFA = FavouirteAdapter(emptyList(), requireContext(), this)

        favouriteFactory = FavouritViewModelFactory(Repository.getInstance(Api_Client(),
            ConcreteLocalSource.getInstance(requireContext())))
        viewModel = ViewModelProvider(this, favouriteFactory).get(FavouritViewModel::class.java)

        viewModel.getWeatherFromRoom()
        lifecycleScope.launch {
            viewModel.weatherFromRoom.collect { result ->
                when (result) {
                    is RoomState.Loading -> {
                        bindingFF.favProgBar.visibility=View.VISIBLE
                        bindingFF.favRV.visibility = View.GONE


                    }
                    is RoomState.Success -> {
                        bindingFF.favProgBar.visibility=View.GONE
                        bindingFF.favRV.visibility = View.VISIBLE

                        result.weather.let {
                            Log.i("TAG", "onViewCreated: $it")

                            adapterFA.submitList(it)
                            bindingFF.favRV.adapter=adapterFA

                            if(it.isEmpty()){
                                bindingFF.noPlacesImg.visibility = View.VISIBLE
                                bindingFF.noPlacesTxt.visibility = View.VISIBLE
                            }else{
                                bindingFF.noPlacesImg.visibility = View.GONE
                                bindingFF.noPlacesTxt.visibility = View.GONE
                            }

                        }}



                    else -> {


                    }
                }


            }
        }






        bindingFF.favFab.setOnClickListener {
            editor.putString("flag", "fav").apply()
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val lat = data?.getStringExtra("lat")?.toDoubleOrNull()
            val lon = data?.getStringExtra("lon")?.toDoubleOrNull()
            if (lat != null) {
                if (lon != null) {
                    val fav = FavouritWeather(lat, lon)
                    viewModel.insertweatherToRoom(fav)
                }
            } else {
                Toast.makeText(context, "empty" + lat, Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {

    }

    override fun onClick(favModel: FavouritWeather) {
        editor.putString("flag", "fav").apply()
        editor.putString("lat", favModel.latitude.toString()).apply()
        editor.putString("lon", favModel.longitude.toString()).apply()
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onDeleteIcon(favWeather: FavouritWeather) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setTitle(R.string.delet_title)
        builder.setMessage(R.string.are_you_sure)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            viewModel.deletweatheFromRoom(favWeather)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }
        builder.show()
    }

}
