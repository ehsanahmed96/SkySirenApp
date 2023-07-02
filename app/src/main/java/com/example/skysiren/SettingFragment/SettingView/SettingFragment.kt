package com.example.skysiren.SettingFragment.SettingView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.skysiren.R
import com.example.skysiren.databinding.FragmentSettingBinding
import kotlinx.coroutines.flow.MutableStateFlow

class SettingFragment : Fragment() {
    lateinit var bindingSF : FragmentSettingBinding
    lateinit var tempStatus : MutableStateFlow<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingSF = FragmentSettingBinding.inflate(inflater,container,false)
        return bindingSF.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         tempStatus =MutableStateFlow<String>("noha")

        bindingSF.locationRadioGroup.setOnCheckedChangeListener { radioGroup, i ->

            val temp = when(i){
                R.id.cel -> "cel"
                R.id.kel -> "kel"
                R.id.fah -> "fah"
                else -> ""
            }
            tempStatus.value = temp
            Log.i("TAG", "onViewCreated: $tempStatus")
        }





    }
    fun getTempStatusFlow(): MutableStateFlow<String> {
        return tempStatus
    }

    companion object {

    }
}