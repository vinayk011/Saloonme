/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network

import com.ezeetech.salonme.BuildConfig
import com.ezeetech.salonme.gson
import com.salonme.base.AUTHORIZATION
import com.salonme.base.DB_SESSION_ID
import io.paperdb.Paper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    private const val CONNECT_TIMEOUT: Long = 5000
    private const val WRITE_TIMEOUT: Long = 5000
    private const val READ_TIMEOUT: Long = 5000

    fun getRestService(session: Boolean, vararg accept: String): Retrofit {
        val b = OkHttpClient.Builder()
        b.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
        b.readTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
        b.writeTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
//        b.sslSocketFactory(SSLManager.getSslSocketFactory(), SSLManager.getTrustManager())
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient: OkHttpClient.Builder =
            b.addInterceptor(logging).addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val sessionId = Paper.book().read<String>(DB_SESSION_ID)
                    val builder = original.newBuilder()
                        .header("Accept", if (accept.isEmpty()) "application/json" else accept[0])
                        .header("User-Agent", "android")
                        .header("version-code", BuildConfig.VERSION_CODE.toString())
                    if (sessionId != null && session) {
                        builder.header(AUTHORIZATION, sessionId)
                    }
                    builder.method(original.method, original.body)
                    return chain.proceed(builder.build())
                }
            })
        okHttpClient.authenticator(TokenAuthenticator())
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEB_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient.build())
            .build()
    }
}