/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import com.ezeetech.salonme.CHANNEL_ID
import com.ezeetech.salonme.CHANNEL_NAME
import com.ezeetech.salonme.databinding.ActivitySplashScreenBinding
import com.ezeetech.salonme.home
import com.ezeetech.salonme.signIn
import com.salonme.base.BaseActivity

class ActivitySplashScreen : BaseActivity<ActivitySplashScreenBinding>() {

    private val handler = Handler()
    override fun onCreate(savedInstance: Bundle?) {
        init()
        super.onCreate(savedInstance)

    }

    private fun init() {
        handler.postDelayed(runnable, 1000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val connectionChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE
        )
        connectionChannel.enableLights(false)
        connectionChannel.enableVibration(false)
        connectionChannel.setShowBadge(false)
        connectionChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val notificationManager =
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        notificationManager.createNotificationChannel(connectionChannel)
    }
    private val runnable = Runnable {
        signIn(this)
        //home(this, null, true)
    }
}