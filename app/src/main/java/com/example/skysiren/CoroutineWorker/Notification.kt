package com.example.skysiren.CoroutineWorker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.skysiren.MainActivity

class Notification (
    var context: Context,
    var description: String,
    //var icon: String,
    var title: String
) : ContextWrapper(context) {

    val CHANNEL_ID = "Channel ID"
    val CHANNEL_NAME = "Channel Name"
    val CHANNEL_DESCRIPTION = "Channel Name"

    private var mManager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

     @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

        channel.enableVibration(true)
        channel.description = CHANNEL_DESCRIPTION
        getManager()?.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getManager(): NotificationManager? {
        if (mManager == null) {
            mManager = getSystemService(NotificationManager::class.java)
        }
        return mManager
    }

    fun getChannelNotification(): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )

      //  val bitmap = BitmapFactory.decodeResource(applicationContext.resources, (icon.length))
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            flags
        )
        return NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setContentTitle(title)
            .setContentText(description) // getText(R.string.open_dialogue)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // you can delete this line
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
//            .setSmallIcon((icon.toInt()))
            // .setSmallIcon(R.drawable.app_icon)
            //.setLargeIcon(bitmap)
    }
}