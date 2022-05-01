package com.example.to_dolist.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.to_dolist.R

const val notificationId = 1
const val channelID = "channel1"


class Notification :BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        val notification= NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Hurry Up!!")
            .setContentText("Only 30 minutes left to complete the task")
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId,notification)
    }

}

