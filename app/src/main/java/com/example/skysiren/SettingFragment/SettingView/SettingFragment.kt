package com.example.skysiren.SettingFragment.SettingView

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.example.skysiren.File_name
import com.example.skysiren.MapActivity
import com.example.skysiren.R
import com.example.skysiren.databinding.FragmentSettingBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*


class SettingFragment : Fragment() {
    lateinit var bindingSF: FragmentSettingBinding
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingSF = FragmentSettingBinding.inflate(inflater, container, false)
        return bindingSF.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireActivity().getSharedPreferences(File_name, Context.MODE_PRIVATE)
        editor = pref.edit()

        var temp = pref.getString("temp", "null")
        when (temp) {
            "metric" -> {
                bindingSF.tempRadioGroup.check(R.id.cel)
            }
            "imperial" -> {
                bindingSF.tempRadioGroup.check(R.id.fah)

            }
            "standard" -> {
                bindingSF.tempRadioGroup.check(R.id.kel)

            }
        }

        var location = pref.getString("location", "null")
        when (location) {
            "gps" -> bindingSF.locationRadioGroup.check(R.id.gps)
            "map" -> bindingSF.locationRadioGroup.check(R.id.map)

        }

        var language = pref.getString("lang", "null")
        when (language) {
            "ar" -> bindingSF.languageRadioGroup.check(R.id.arabic)
            "en" -> bindingSF.languageRadioGroup.check(R.id.english)
        }

        val windSpeed = pref.getString("measureUnit", "null")
        when (windSpeed) {
            "m/s" -> bindingSF.WindRadioGroup.check(R.id.meter_sec)
            "m/h" -> bindingSF.WindRadioGroup.check(R.id.mile_hour)
        }
        val noti = pref.getBoolean("noti", false)
        when (noti) {
            true -> bindingSF.notificationRadioGroup.check(R.id.enable)
            false -> bindingSF.notificationRadioGroup.check(R.id.disable)
        }

        bindingSF.languageRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.arabic ->{
                    editor.putString("lang", "ar").apply()
                   setLanguage(requireContext(),"ar")
                  activity?.recreate();
                }
                R.id.english -> {
                    editor.putString("lang", "en").apply()
                   setLanguage(requireContext(),"en")
                    activity?.recreate();

                }
            }
        }
        bindingSF.WindRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.meter_sec -> editor.putString("measureUnit", "m/s").apply()
                R.id.mile_hour -> editor.putString("measureUnit", "m/h").apply()
            }
        }

        bindingSF.tempRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.cel -> editor.putString("temp", "metric").apply()
                R.id.kel -> editor.putString("temp", "standard").apply()
                R.id.fah -> editor.putString("temp", "imperial").apply()
            }
        }

        bindingSF.locationRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.gps -> editor.putString("location", "gps").apply()
                R.id.map -> {
                    editor.putString("location", "map").apply()
                    editor.putString("flag","setting").apply()
                    val intent = Intent(requireContext(), MapActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
        bindingSF.notificationRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.enable -> editor.putBoolean("noti", true).apply()
                R.id.disable -> editor.putBoolean("noti", false).apply()
            }
        }


    }
    fun  setLanguage(context: Context, language:String) {

        val locale = Locale(language)
        val config = context.resources.configuration
        config.locale= Locale(language)
        Locale.setDefault(locale)
        config.setLayoutDirection(Locale(language))
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        context.createConfigurationContext(config)

    }

    companion object {

    }
}