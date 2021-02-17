package com.`in`.ezeetech.social

import android.app.Application
import android.util.Log
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

/**
 * Created by Vinay.
 * Copyright (c) 2021 EzeeTech. All rights reserved.
 */

object SocialManager {
    private lateinit var application: Application

    fun init(app: Application) {
        application = app
        val config = TwitterConfig.Builder(application)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    application.getString(R.string.TWITTER_CONSUMER_KEY),
                    application.getString(R.string.TWITTER_CONSUMER_SECRET)
                )
            )
            .debug(true)
            .build()
        Twitter.initialize(config)

    }

    fun getApplication(): Application {
        return application
    }
}