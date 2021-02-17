package com.`in`.ezeetech.social

import android.content.Context
import android.content.Intent

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

internal class GoogleLoginClient : LoginClient {
    private var callback: LoginResponse? = null
    private var googleSignInClient: GoogleSignInClient? = null

    override fun init(loginResponse: LoginResponse) {
        callback = loginResponse
    }

    override fun login(context: Context): Boolean {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return if (account != null && account.idToken != null) {
            callback?.success(getUser(account, account.idToken ?: ""))
            true
        } else {
            val signInIntent = googleSignInClient?.signInIntent
            (context as ActivitySocial).startActivityForResult(signInIntent, RC_SIGN_IN)
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            if (account != null && account.idToken != null) {
                callback?.success(getUser(account, account.idToken ?: ""))
            } else {
                callback?.failed(null)
            }
        } catch (error: ApiException) {
            callback?.failed(error.localizedMessage)
        }

    }

    override fun refresh() {
        val account = GoogleSignIn.getLastSignedInAccount(SocialManager.getApplication())
        if (account != null && account.idToken != null) {
            callback?.active()
        } else {

        }
    }

    override fun logout() {
        googleSignInClient?.signOut()
            ?.addOnCompleteListener { _ ->
                callback?.logout()
            }
    }

    private fun getUser(account: GoogleSignInAccount, token: String): SocialResponse {
        return SocialResponse(
            id = account.id.toString(),
            name = account.displayName.toString(),
            imageUrl = account.photoUrl?.toString(),
            accessToken = token,
            email = account.email.toString(),
            mobileNumber = ""
        )
    }
}

