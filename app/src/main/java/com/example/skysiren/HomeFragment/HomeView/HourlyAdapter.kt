package com.example.skysiren.HomeFragment.HomeView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skysiren.R

class HourlyAdapter /*: ListAdapter<Product, ViewHolder>(ProductDiffUtil())*/ {

//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater : LayoutInflater =
//            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = inflater.inflate(R.layout.item_product , parent ,false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val currentObj = getItem(position)
//        holder.productName.text = currentObj.title
//        holder.productBrand.text=currentObj.brand
//        holder.productPrice.text=currentObj.price.toString()
//        holder.productDesc.text=currentObj.description
//        holder.rating.rating=currentObj.rating
//        Glide.with(holder.itemView.context)
//            .load(currentObj.thumbnail)
//            .placeholder(R.drawable.ic_launcher_background)
//            .error(R.drawable.ic_launcher_foreground)
//            .into(holder.productImg)
//
//    }
//
//}
//
//
//
//class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//
//}
//
//class ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
//    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return oldItem === newItem
//    }
//
//    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return  oldItem == newItem
//    }
//
}