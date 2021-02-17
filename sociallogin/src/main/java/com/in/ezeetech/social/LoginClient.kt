package com.`in`.ezeetech.social

import android.content.Context
import android.content.Intent

internal interface LoginClient {
    fun init(loginResponse: LoginResponse)

    fun login(context: Context): Boolean

    fun logout()

    fun refresh()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}


internal interface LoginResponse {
    fun success(socialResponse: SocialResponse)
    fun active()
    fun canceled()
    fun failed(error: String?)
    fun token(token: String)
    fun logout()
}