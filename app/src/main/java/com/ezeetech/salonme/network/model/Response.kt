/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network.model

import com.ezeetech.salonme.model.Slider
import com.google.gson.annotations.SerializedName


data class SignInResponse(
    val success: Boolean,
    val accessToken: String,
    @SerializedName("user_data")
    val userData: UserData,
    val message: String
)

data class SignUpResponse(
    val success: Boolean,
    val user_id: Int,
    val message: String
)

data class UserData(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName : String,
    @SerializedName("mobile_number")
    val mobile: String,
    @SerializedName("email_address")
    val email: String,
    @SerializedName("user_id")
    val userId: String
)

data class SlidersResponse(
    val success: Boolean,
    val message: String,
    val sliders: List<Slider>
)