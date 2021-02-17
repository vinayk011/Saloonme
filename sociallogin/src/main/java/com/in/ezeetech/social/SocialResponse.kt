package com.`in`.ezeetech.social

data class SocialResponse(
    val id: String,
    val name: String,
    val email: String,
    val mobileNumber: String,
    val accessToken: String,
    var imageUrl: String?
)