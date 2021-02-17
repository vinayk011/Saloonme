/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network.model



data class SignInRequest(val username: String, val password: String/*, val appId: String*/)

data class SignUpRequest(
    val first_name: String,
    val last_name: String,
    val mobile_number: String,
    val password: String,
    val email: String,
    val data_ofbirth: String
)

data class UserRequest(
    val mobile: String,
    val name: String,
    val email: String
)