package com.example.skysiren.FavouritesFragment.FavouritesView

import android.content.ContentValues
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.skysiren.Model.FavouritWeather
import com.example.skysiren.Model.WeatherDetail
import com.example.skysiren.databinding.FavRowItemBinding
import java.util.*

class FavouirteAdapter  (var list :List< FavouritWeather>,
                         val context: Context,
                         private var onClick:OnClick
) : RecyclerView.Adapter<FavouirteAdapter.ViewHolder>(){

    private lateinit var binding: FavRowItemBinding
    inner class ViewHolder(var binding: FavRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavRowItemBinding.inflate(inflater, parent, false)
        Log.i(ContentValues.TAG, "onCreateViewHolder: favourite adapter ")
        return ViewHolder(binding)
    }

    fun submitList(listWeather: List<FavouritWeather>) {
        this.list = listWeather
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list != null) {
            var fav: FavouritWeather = list[position]
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: MutableList<Address> = geocoder.getFromLocation(
                fav.latitude, fav.longitude, 1
            ) as MutableList<Address>
            if (addresses.isNotEmpty()) {
                holder.binding.txtCountryName.text = "${addresses[0].adminArea} / ${addresses[0].countryName}"
            } else {
                Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show()
            }
            holder.binding.iconDel.setOnClickListener { onClick.onDeleteIcon(fav) }
            holder.binding.favCard.setOnClickListener {  onClick.onClick(fav) }
        }
    }

}



