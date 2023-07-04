package com.example.skysiren.AlertFragment.AlertView

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skysiren.AlertFragment.AlertViewModel.AlertViewModel
import com.example.skysiren.AlertFragment.AlertViewModel.AlertViewModelFactory
import com.example.skysiren.DataBase.ConcreteLocalSource
import com.example.skysiren.DataBase.RoomState
import com.example.skysiren.FavouritesFragment.FavouritesModelView.FavouritViewModel
import com.example.skysiren.FavouritesFragment.FavouritesModelView.FavouritViewModelFactory
import com.example.skysiren.FavouritesFragment.FavouritesView.FavouirteAdapter
import com.example.skysiren.File_name
import com.example.skysiren.Model.Alerts
import com.example.skysiren.Model.Repository
import com.example.skysiren.Network.Api_Client
import com.example.skysiren.R
import com.example.skysiren.databinding.FragmentAlertBinding
import com.example.skysiren.databinding.SpecifingAlertBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AlertFragment : Fragment(), OnClick {
    lateinit var bindingAF: FragmentAlertBinding
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var adapterAA: AlertAdapter
    lateinit var alertView: View
    lateinit var alertObj : Alerts
    lateinit var viewModel: AlertViewModel
    lateinit var alertFactory: AlertViewModelFactory
    lateinit var alertBinding: SpecifingAlertBinding  ///// for specifing alert view
     lateinit var startDate: String
   lateinit var endDate:String
    lateinit var startTime: String
    lateinit var endTime: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingAF = FragmentAlertBinding.inflate(inflater, container, false)
        return bindingAF.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertObj = Alerts(1,1,2 , 2)
        alertView = view
        pref = requireActivity().getSharedPreferences(File_name, Context.MODE_PRIVATE)
        editor = pref.edit()
        var lang = pref.getString("lang", "en")
        adapterAA = lang?.let {
            AlertAdapter(requireContext(), emptyList(), it, this)
        }!!

        alertFactory = AlertViewModelFactory(Repository.getInstance(Api_Client(),
            ConcreteLocalSource.getInstance(requireContext())))
        viewModel = ViewModelProvider(this, alertFactory).get(AlertViewModel::class.java)


        val dialog = Dialog(alertView.context)
        alertBinding = SpecifingAlertBinding.inflate(layoutInflater)
        dialog.setContentView(alertBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewModel.getAlertsfromRoom()
        lifecycleScope.launch() {
            viewModel.alertRoom.collect {result->
                when(result) {
                    is AlertState.Loading -> {
                        bindingAF.alertProgBar.visibility=View.VISIBLE
                        bindingAF.alertRV.visibility = View.GONE


                    }
                    is AlertState.Success -> {
                        bindingAF.alertProgBar.visibility=View.GONE
                        bindingAF.alertRV.visibility = View.VISIBLE

                        result.alert.let {
                            Log.i("TAG", "onViewCreated: $it")

                            adapterAA.submitList(it)
                            bindingAF.alertRV.adapter=adapterAA

                            if(it.isEmpty()){
                                bindingAF.noAlertImg.visibility = View.VISIBLE
                                bindingAF.noAlertsTxt.visibility = View.VISIBLE
                            }else{
                                bindingAF.noAlertImg.visibility = View.GONE
                                bindingAF.noAlertsTxt.visibility = View.GONE
                            }

                        }}



                    else -> {


                    }
                }
            }
        }

        bindingAF.AlertFab.setOnClickListener {
            dialog.show()
        }
        alertBinding.saveAlertsDataBtn.setOnClickListener {
            askForDrawOverlaysPermission {
                if (alertBinding.startDateValue.text.isEmpty()) {
                    Toast.makeText(requireContext(), "start date is required", Toast.LENGTH_SHORT).show()
                } else if (alertBinding.endDateValue.text.isEmpty()) {
                    Toast.makeText(requireContext(), "end date is required", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insertAlertToRoom(alertObj)
                    alertBinding.startDateValue.setText("")
                    alertBinding.endDateValue.setText("")
                    Toast.makeText(requireContext(), "making alert successfully", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }
            }
        }

        alertBinding.startDateValue.setOnClickListener{
            setStartDateAndTime{(timeInMillis, dateInMillis) ->
                startTime=getTimeToAlert(timeInMillis,"en")
                startDate=getDateToAlert(dateInMillis,"en")
                alertBinding.startDateValue.setText("$startDate \n $startTime")
                alertObj.startDate = dateInMillis
                alertObj.startimeOfAlert = timeInMillis
                Log.d("Alarm", "Time: $timeInMillis, Date: $dateInMillis")
            }



        }
        alertBinding.endDateValue.setOnClickListener{
            setEndDateAndTime{(timeInMillis, dateInMillis) ->
                endTime=getTimeToAlert(timeInMillis,"en")
                endDate=getDateToAlert(dateInMillis,"en")
                alertBinding.endDateValue.setText("$endDate \n $endTime")
                alertObj.endDate = dateInMillis
                alertObj.endTimeOfAlert = timeInMillis
                Log.d("Alarm", "Time: $timeInMillis, Date: $dateInMillis")
            }

        }

    }
    private fun setStartDateAndTime(callback: (Pair<Long, Long>) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        requireContext(),
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis to this.timeInMillis.dateToLong())
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }
    }
    private fun setEndDateAndTime(callback: (Pair<Long, Long>) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        requireContext(),
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis to this.timeInMillis.dateToLong())
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }
    }
    fun Long.dateToLong(): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
    fun getDateToAlert(time: Long, language: String): String{
        return SimpleDateFormat("M/d/yyyy",Locale(language)).format(time)
    }

    fun getTimeToAlert(time: Long, language: String): String {
        return SimpleDateFormat("h:mm a",Locale(language)).format(time)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun askForDrawOverlaysPermission(callback: () -> Unit) {
        if (!Settings.canDrawOverlays(requireView().context)) {
            AlertDialog.Builder(requireView().context)
                .setTitle(R.string.permission)
                .setMessage(R.string.permission_required)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                    intent.setClassName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity"
                    )
                    intent.putExtra("extra_pkgname", requireView().context.packageName)
                    runtimePermissionResultLauncher.launch(intent)
                }
                .setIcon(R.drawable.permission)
                .show()
        } else {
            callback() // Call the callback if the permission is already granted
        }
    }



    private val runtimePermissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }




    companion object {

    }

    override fun onDelet(alerts: Alerts) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setTitle(getString(R.string.delete_alert_title))
        builder.setMessage(getString(R.string.are_you_sure_alert))
        builder.setPositiveButton(getString(R.string.confirm_yes)) { _, _ ->
            viewModel.deletAlertFromRoom(alerts)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }

        builder.show()
    }
}