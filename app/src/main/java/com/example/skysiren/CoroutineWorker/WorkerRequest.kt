package com.example.skysiren.CoroutineWorker

import android.content.Context
import android.util.Log
import com.example.skysiren.Model.TypeConverter
import com.example.skysiren.Model.Alerts
import androidx.work.*
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.TimeUnit

object WorkerRequest {
    fun createRequst(
        alert: Alerts,
        desc: String,
        icon: String,
        context: Context,
        alertOrNotifi:String,
        startTimeOfAlert: Long,
    ) {

        Log.i("TAG", "createRequst: enter work request")
        val requestData = Data.Builder()
            .putString("alertobj", convertAlertToString(alert))
            .putString("desc", desc)
            .putString("icon", icon)
            .putLong("startTimeOfAlert", startTimeOfAlert)
            .putString("isAlertOrNotifi" , alertOrNotifi)
            .build()

        val request = OneTimeWorkRequestBuilder<CouroutineWorker>()
            .setInitialDelay(startTimeOfAlert - Calendar.getInstance().timeInMillis,
                TimeUnit.MILLISECONDS)
            .setInputData(requestData)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build())
            .addTag(alert.startDate.toString())
            .build()

        WorkManager
            .getInstance(context)
            .enqueue( request)
        Log.i("TAG", "createRequst: enqueue  work request ${alert.startDate}")


    }

    fun remove(tag: String, context: Context) {
        val worker = WorkManager.getInstance(context)
        worker.cancelAllWorkByTag(tag)
    }

    @androidx.room.TypeConverter
    fun convertAlertToString(myAlert: Alerts): String = Gson().toJson(myAlert)
}