package com.example.skysiren.CoroutineWorker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Global.getString
import android.util.Log
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
import com.example.skysiren.Model.Alerts
import com.example.skysiren.Model.Repository
import com.example.skysiren.Model.RepositoryInterface
import com.example.skysiren.Network.Api_Client
import com.example.skysiren.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
        val isNotifi = pref.getBoolean("noti", false)
        Log.i("TAG", "doWork:${isNotifi.toString()} ")

        val isAlert = pref.getBoolean("alert", false)
        Log.i("TAG", "doWork:${isAlert.toString()} ")


        val alert = inputData.getString("alertobj")?.let { convertToUserAlert(it) }
        Log.i("TAG", "doWork if alert data: $alert")
        val desc = inputData.getString("desc")
        val icon = inputData.getString("icon")
        val time = inputData.getLong("startTimeOfAlert", 0L)
        Log.i("TAG", "doWork:${alert?.isAlert} ")

        repo = Repository.getInstance(Api_Client(), ConcreteLocalSource(applicationContext))

        if (alert != null) {
            Log.i("TAG", "doWork: alert not equal null")
            if (System.currentTimeMillis() >= alert.startimeOfAlert && System.currentTimeMillis() <= alert.endTimeOfAlert) {
                if (isNotifi) {
                    Log.i("TAG", "doWork: enter the notifi scoope")
                    createNotificationChannel()
                    val builder = NotificationCompat.Builder(context, channel_id)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("my notification")
                        .setContentText("Dwonloading...")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                    builder.setProgress(0, 0, true)
                    builder.setOngoing(true)
                    editor.putBoolean("noti", false).apply()
                    val notificationManager =
                       context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(11, builder.build())
                }
                Log.i("TAG", "doWork: enter the time")
                if (alert.isAlert == "i") {
                    if (Settings.canDrawOverlays(context)) {
                        Log.i("TAG", "doWork: alert is making in do work alert scoop")
                        withContext(Dispatchers.Main) {
                            val intent = Intent(context, AlertPopUpActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                            intent.putExtra("desc", desc)
                            intent.putExtra("icon", icon)
                            context.startActivity(intent)
                        }
                        editor.putBoolean("alert", false).apply()

                    }
                }
                WorkerRequest.createRequst(
                    alert,
                    desc.toString(),
                    icon.toString(),
                    context,
                    (time + 86400000)
                )

            } else {
                repo.deletAlertFromRoom(alert)
                WorkerRequest.remove(alert.startDate.toString(), context)
            }
        }
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
}