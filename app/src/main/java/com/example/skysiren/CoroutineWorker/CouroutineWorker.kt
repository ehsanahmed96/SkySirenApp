package com.example.skysiren.CoroutineWorker

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.TypeConverter
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.skysiren.AlertPopUpActivity
import com.example.skysiren.DataBase.ConcreteLocalSource
import com.example.skysiren.File_name
import com.example.skysiren.Model.Alert
import com.example.skysiren.Model.Alerts
import com.example.skysiren.Model.Repository
import com.example.skysiren.Model.RepositoryInterface
import com.example.skysiren.Network.Api_Client
import com.example.skysiren.R
import com.example.skysiren.databinding.ActivityAlertPopUpBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type


const val channel_id = "channelId"

class CouroutineWorker(private val context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    lateinit var pref: SharedPreferences
    lateinit var editor: Editor
    lateinit var repo: RepositoryInterface

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun doWork(): Result {
        Log.i("TAG", "doWork: start of do work method0")

        pref = context.getSharedPreferences(File_name, Context.MODE_PRIVATE)
        editor = pref.edit()


        val alert = inputData.getString("alertobj")?.let { convertToUserAlert(it) }
        val desc = inputData.getString("desc")
        val icon = inputData.getString("icon")
        val isAlertOrNotifi = inputData.getString("isAlertOrNotifi")
        Log.i("TAG", "doWork: $isAlertOrNotifi")


        repo = Repository.getInstance(Api_Client(), ConcreteLocalSource(applicationContext))

        if (alert != null) {
            if (System.currentTimeMillis() >= alert.startimeOfAlert && System.currentTimeMillis() <= alert.endTimeOfAlert) {
                if (isAlertOrNotifi == "notifi") {
                    Log.i("TAG", "doWork: enter the notifi scoope")
                    val soundUri = Uri.parse("android.resource://${context.packageName}/${R.raw.alert_sound}")
                    createNotificationChannel()
                    val builder = NotificationCompat.Builder(context, channel_id)
                        .setSmallIcon(R.drawable.cloudy)
                        .setContentTitle("Notification SkySiren")
                        .setContentText(desc)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setVibrate(longArrayOf(0, 1000, 500, 1000))
                        .setLights(Color.BLUE, 2000, 1000)
                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(11, builder.build())

                } else if (isAlertOrNotifi == "alert") {
                    if (Settings.canDrawOverlays(context)) {
                        withContext(Dispatchers.Main) {
//                            val intent = Intent(context, AlertPopUpActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
//                            intent.putExtra("desc", desc)
//                            intent.putExtra("icon", icon)
//                            context.startActivity(intent)
                            val context = applicationContext ?: return@withContext Result.failure()
                            val themeContext = ContextThemeWrapper(context, R.style.Theme_SkySiren)
                            showAlertDialog(themeContext, desc!!, icon!!)

                        }

                    }
                }


            }
        }
        if (alert != null) {
            (repo as Repository).deletAlertFromRoom(alert)
        }
        WorkerRequest.remove(alert?.startDate.toString(), context)
        return Result.success()
    }


    @TypeConverter
    fun convertToUserAlert(value: String): Alerts {
        val type: Type = object : TypeToken<Alerts>() {}.type
        return Gson().fromJson(value, type)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notification sky siren"
            val description = "describtion of weather"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channel_id, name, importance)
            channel.description = description
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showAlertDialog(context: Context, desc: String, icon: String) {
        val alertDialog =
            ActivityAlertPopUpBinding.inflate(LayoutInflater.from(context))
        val mediaPlayer = MediaPlayer.create(context, R.raw.alert)

        AlertDialog.Builder(context).create().apply {
            setView(alertDialog.root)

            alertDialog.alertDesc.text = desc
            Picasso.get().load(R.drawable.cloudy).into(alertDialog.image)
            alertDialog.btnDismiss.setOnClickListener {
                dismiss()
            }
            mediaPlayer.isLooping = true
            mediaPlayer.start()
            setOnDismissListener {
                mediaPlayer.release()
            }
            window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.CENTER)
            show()
        }

    }

}