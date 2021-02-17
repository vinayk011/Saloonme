package com.`in`.ezeetech.social

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.widget.LoginButton
import com.google.gson.Gson
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import android.widget.ImageView
import com.salonme.base.LOGOUT
import com.salonme.base.REFRESH
import com.salonme.base.TYPE
import com.salonme.base.Trace


const val RC_SIGN_IN = 99

class ActivitySocial : AppCompatActivity() {
    private lateinit var fbLogin: LoginButton
    private lateinit var twitterLogin: TwitterLoginButton
    private lateinit var closeButton: ImageView
    private val socialHandler = SocialHandler()
    private val type: String by lazy {
        intent.extras?.getString(TYPE) ?: ""
    }

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_social)
        twitterLogin = findViewById(R.id.sign_in_twitter)
        twitterLogin.callback = twitterCallback
        fbLogin = findViewById(R.id.sign_in_facebook)
        fbLogin.setPermissions("public_profile", "email")
        closeButton = findViewById(R.id.close_button)
        closeButton.setOnClickListener {
            callback.canceled()
        }

        val refresh = intent.extras?.getBoolean(REFRESH) ?: false
        val logout = intent.extras?.getBoolean(LOGOUT) ?: false
        if (type.isNotBlank()) {
            socialHandler.initLoginClient(type, callback)
            if (refresh && !logout) {
                socialHandler.getLoginClient().refresh()
            } else {
                if (!socialHandler.getLoginClient().login(this) && !logout) {
                    when (type) {
                        GOOGLE -> {
                        }
                        FACEBOOK -> fbLogin.performClick()
                        TWITTER -> twitterLogin.performClick()
                        else -> {
                            Toast.makeText(
                                this,
                                getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    }
                }
                if (logout) {
                    socialHandler.getLoginClient().logout()
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (type == TWITTER) {
            twitterLogin.onActivityResult(requestCode, resultCode, data)
        }
        socialHandler.getLoginClient().onActivityResult(requestCode, resultCode, data)
    }

    private val callback = object : LoginResponse {
        override fun logout() {
            val intent = Intent()
            intent.putExtra("logout", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        override fun success(socialResponse: SocialResponse) {
            Trace.e(Gson().toJson(socialResponse))
            val intent = Intent()
            intent.putExtra("type", type)
            intent.putExtra("user", Gson().toJson(socialResponse))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        override fun active() {
            Trace.e("")
            val intent = Intent()
            intent.putExtra("type", type)
            intent.putExtra("isActive", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        override fun canceled() {
            Trace.e("")
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        override fun failed(error: String?) {
            Trace.e("$error")
            val intent = Intent()
            intent.putExtra("failed", error)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

        override fun token(token: String) {
            Trace.e(token)
            val intent = Intent()
            intent.putExtra("type", type)
            intent.putExtra("token", token)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private val twitterCallback = object : Callback<TwitterSession>() {
        override fun success(result: Result<TwitterSession>) {
            socialHandler.getLoginClient().login(this@ActivitySocial)
        }

        override fun failure(exception: TwitterException) {
            callback.failed(exception.localizedMessage)
        }
    }
}