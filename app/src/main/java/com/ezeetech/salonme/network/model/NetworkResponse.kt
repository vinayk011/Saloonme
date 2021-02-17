/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network.model


sealed class NetworkResponse<out T : Any> {
    data class Success<out T : Any>(val response: T?) : NetworkResponse<T>()
    data class Error(val error: List<NetworkError>) : NetworkResponse<Nothing>()
    data class Exception(val t: Throwable?) : NetworkResponse<Nothing>()
}