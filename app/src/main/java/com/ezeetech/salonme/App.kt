/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme;

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.salonme.base.BaseApp

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        init(this)
        BaseApp.init(this)

    }

    companion object {
        lateinit var application: Application
        fun init(app: Application) {
            application = app
        }
    }
}