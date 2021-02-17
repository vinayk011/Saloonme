/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ezeetech.salonme.ui.ActivitySplashScreen
import com.salonme.base.SESSION_CLOSED


const val CHANNEL_ID = "SALONME"
const val CHANNEL_NAME = "General"
private const val ONGOING_NOTIFICATION_ID = 1
private const val PAIRING_NOTIFICATION_ID = 2
private const val LOG_OUT_NOTIFICATION_ID = 3
private const val LEADER_BOARD_NOTIFICATION_ID = 4

object NotificationUtil {

    /*private fun setNotification(context: Context): Notification {
        val notificationIntent = Intent(context, ActivitySplashScreen::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val notification: Notification
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification =
                Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setShowWhen(false)
                    .setVisibility(Notification.VISIBILITY_PRIVATE)
                    .build()
        } else {
            notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                    .build()
        }
        return notification
    }*/

    fun remove() {
        notificationManager.cancel(ONGOING_NOTIFICATION_ID)
    }

    private val notificationManager: NotificationManager by lazy {
        App.application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

//    fun setLeaderBoardNotification(context: Context, message: String) {
//        val notificationIntent = Intent(context, ActivitySplashScreen::class.java)
//        notificationIntent.putExtra("context", FRIENDS)
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            1,
//            notificationIntent,
//            PendingIntent.FLAG_CANCEL_CURRENT
//        )
//
//        val notification: Notification
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notification = Notification.Builder(context, CHANNEL_ID)
//                .setContentTitle(context.getString(R.string.app_name))
//                .setContentText(message)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent)
//                .setOngoing(false)
//                .setShowWhen(true)
//                .setAutoCancel(true)
//                .setVisibility(Notification.VISIBILITY_PRIVATE)
//                .build()
//        } else {
//            notification = NotificationCompat.Builder(context, CHANNEL_ID)
//                    .setContentTitle(context.getString(R.string.app_name))
//                    .setContentText(message)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentIntent(pendingIntent)
//                    .setOngoing(false)
//                    .setShowWhen(true)
//                    .setAutoCancel(true)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
//                    .build()
//        }
//        val notificationManager = NotificationManagerCompat.from(context)
//        notificationManager.notify(LEADER_BOARD_NOTIFICATION_ID, notification)
//    }

   /* fun setLogoutNotification(context: Context, message: String) {
        val notificationIntent = Intent(context, ActivitySplashScreen::class.java)
        notificationIntent.putExtra("context", SESSION_CLOSED)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val notification: Notification
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification =
                Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setShowWhen(false)
                    .setVisibility(Notification.VISIBILITY_PRIVATE)
                    .build()
        } else {
            notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .setShowWhen(false)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                    .build()
        }
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(LOG_OUT_NOTIFICATION_ID, notification)
    }*/
}