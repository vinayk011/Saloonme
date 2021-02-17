package com.`in`.ezeetech.social

import android.content.Context
import android.content.Intent

import com.twitter.sdk.android.core.models.User

import com.twitter.sdk.android.core.*


internal class TwitterLoginClient : LoginClient {
    private var callback: LoginResponse? = null

    override fun init(loginResponse: LoginResponse) {
        callback = loginResponse
    }

    override fun login(context: Context): Boolean {
        val session = TwitterCore.getInstance().sessionManager.activeSession
        return if (session != null && session.authToken != null && session.authToken.token != null) {
            getUserProfile(session.authToken.token)
            true
        } else {
            false
        }
    }

    override fun refresh() {
        val session = TwitterCore.getInstance().sessionManager.activeSession
        if (session != null && session.authToken != null && session.authToken.token != null) {
            callback?.active()
        } else {
            callback?.logout()
        }
    }

    override fun logout() {
        if (TwitterCore.getInstance().sessionManager.activeSession != null) {
            TwitterCore.getInstance().sessionManager.clearActiveSession()
        }
        callback?.logout()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    private fun getUserProfile(token: String) {
        val twitterApiClient = TwitterCore.getInstance().apiClient
        val accountService = twitterApiClient.accountService
        val call = accountService.verifyCredentials(true, true, true)
        call.enqueue(object : Callback<User>() {
            override fun success(result: Result<User>) {
                val user = result.data
                try {
                    callback?.success(getUser(result.data, token))
                } catch (e: Exception) {
                    callback?.failed(e.toString())
                }

            }

            override fun failure(exception: TwitterException) {}
        })
    }

    private fun getUser(user: User, token: String): SocialResponse {
        return SocialResponse(
            id = user.id.toString(),
            name = user.name,
            imageUrl = user.profileImageUrlHttps.replace("_normal", ""),
            accessToken = token,
            email = user.email,
            mobileNumber = ""
        )
    }
}
