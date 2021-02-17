/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network


import com.salonme.base.PREF_RENEW
import com.salonme.base.Preference
import com.ezeetech.salonme.network.model.SignInRequest
import com.salonme.base.*
import io.paperdb.Paper
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401 && !Preference.instance.getBoolean(PREF_RENEW)) {
            Preference.instance.putBoolean(PREF_RENEW, true)
            refreshToken()?.let {
                return response.request.newBuilder()
                    .header(ACCESS_TOKEN, it)
                    .build()
            }
        }
        Preference.instance.remove(PREF_RENEW)
        return null
    }

    private fun refreshToken(): String? {
        var sessionID: String? = null
        val signInRequest = Paper.book().read<SignInRequest>(DB_USER)
        if (signInRequest != null) {
            val refreshResponse = RetrofitService
                .getRestService(false).create(RestService::class.java)
                .renew(signInRequest).execute()
            if (refreshResponse.code() == 200) {
                for (name: String in refreshResponse.headers().names()) {
                    if (name == ACCESS_TOKEN) {
                        Paper.book().write(DB_SESSION_ID, refreshResponse.headers()[name])
                        sessionID = refreshResponse.headers()[name]
                    }
                }
            } else {
                genericMessage(SESSION_EXIT, true)
            }
        }
        return sessionID
    }
}