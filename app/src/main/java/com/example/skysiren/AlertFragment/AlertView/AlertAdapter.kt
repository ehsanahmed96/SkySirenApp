package com.example.skysiren.AlertFragment.AlertView

import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.skysiren.Model.Alerts
import com.example.skysiren.databinding.AlertRowItemBinding
import java.text.SimpleDateFormat
import java.util.*


class AlertAdapter(
    private val context: Context,
    private var AlertsList: List<Alerts>,
    private val lang: String,
    private val onClick: OnClick,
) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    private lateinit var binding: AlertRowItemBinding


    inner class ViewHolder(var binding: AlertRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AlertRowItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       if(AlertsList!=null){
           var alert :Alerts = AlertsList[position]
           holder.binding.startDateValue.text= convertTimeToString(alert.startDate ,"dd/MM/yyyy" ,lang)
           holder.binding.endDateValue.text = convertTimeToString(alert.endDate ,"dd/MM/yyyy" ,lang)
           holder.binding.startTimeValue.text = convertTimeToString(alert.startimeOfAlert ,"HH:mm a" ,lang)
           holder.binding.endTimeValue.text = convertTimeToString(alert.endTimeOfAlert ,"HH:mm a" ,lang)

           holder.binding.dismiss.setOnClickListener {
               onClick.onDelet(alert)
           }
       }
    }

    fun submitList(list: List<Alerts>) {
        AlertsList = list
    }

    override fun getItemCount(): Int {
        return AlertsList.size
    }

    fun convertTimeToString(time:Long,formate :String , lang:String) :String{
        val formatter = SimpleDateFormat(formate, Locale(lang))
        val calender = Calendar.getInstance()
        calender.timeInMillis = time
        return formatter.format(calender.time)
    }
}