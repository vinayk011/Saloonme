package com.`in`.ezeetech.social

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONException
import org.json.JSONObject

internal class FacebookLoginClient : LoginClient {
    private var callbackManager: CallbackManager? = null
    private var callback: LoginResponse? = null

    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            if (loginResult.accessToken != null) {
                getUserProfile(loginResult.accessToken)
            } else {
                callback?.failed(null)
            }
        }

        override fun onCancel() {
            callback?.canceled()
        }

        override fun onError(error: FacebookException) {
            callback?.failed(error = error.localizedMessage)
        }
    }

    override fun init(loginResponse: LoginResponse) {
        callback = loginResponse
    }

    override fun login(context: Context): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        return if (isLoggedIn) {
            getUserProfile(accessToken)
            true
        } else {
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().registerCallback(callbackManager, facebookCallback)
            false
        }
    }

    override fun refresh() {
        if (AccessToken.isCurrentAccessTokenActive()) {
            callback?.active()
        } else {
            AccessToken.refreshCurrentAccessTokenAsync(object :
                AccessToken.AccessTokenRefreshCallback {
                override fun OnTokenRefreshed(accessToken: AccessToken?) {
                    if (accessToken != null) {
                        callback?.token(accessToken.token)
                    } else {
                        callback?.failed(null)
                    }
                }

                override fun OnTokenRefreshFailed(exception: FacebookException?) {
                    callback?.failed(exception?.localizedMessage)
                }
            })
        }
    }

    override fun logout() {
        LoginManager.getInstance().logOut()
        callback?.logout()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun getUserProfile(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { json, _ ->
            try {
                callback?.success(getUser(json, accessToken.token))
            } catch (e: JSONException) {
                callback?.failed(e.toString())
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "name, email, id, birthday")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun getUser(graph: JSONObject, token: String): SocialResponse {
        return SocialResponse(
            id = graph.getString("id"),
            name = graph.getString("name"),
            imageUrl = "https://graph.facebook.com/" + graph.getString("id") + "/picture?type=large",
            accessToken = token,
            email = if (graph.has("email")) {
                graph.getString("email")
            } else {
                ""
            },
            mobileNumber = ""
        )
    }
}