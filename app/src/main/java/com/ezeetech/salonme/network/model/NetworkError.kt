/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network.model



data class NetworkError(
    val description: String,
    val recommendation: String?
) {
    fun asString(): String {
        recommendation?.let {
            return "$description\n$recommendation"
        }
        return description
    }
}

