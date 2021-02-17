/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.salonme.base;

import android.app.Application
import io.paperdb.Paper

object BaseApp {
    private lateinit var application: Application

    fun init(app: Application) {
        application = app
        Preference.init(application)
        Paper.init(application)
    }

    fun getApplication(): Application {
        return application
    }
}

