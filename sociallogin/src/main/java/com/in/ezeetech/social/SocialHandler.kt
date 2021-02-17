package com.`in`.ezeetech.social

const val GOOGLE = "google"
const val FACEBOOK = "facebook"
const val TWITTER = "twitter"

class SocialHandler {
    private lateinit var loginClient: LoginClient

    internal fun initLoginClient(type: String, callback: LoginResponse) {
        when (type) {
            FACEBOOK -> loginClient = FacebookLoginClient()
            GOOGLE -> loginClient = GoogleLoginClient()
            TWITTER -> loginClient = TwitterLoginClient()
        }
        loginClient.init(callback)
    }

    internal fun getLoginClient(): LoginClient {
        return loginClient
    }
}