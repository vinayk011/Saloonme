/*
 *  Created by Vinay on 27-1-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network_call

import android.app.Application
import com.ezeetech.salonme.network.model.*
import com.salonme.base.ACCESS_TOKEN
import com.salonme.base.DB_SESSION_ID
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpNetworkCall(application: Application) :
    NetworkModelWithSession<SignUpResponse, SignUpRequest, SignUpResponse>(application),
    Callback<SignUpResponse> {
    override fun execute(req: SignUpRequest) {
        onDestroy()
        restServices.signUp(req).let {
            call = it
            it.enqueue(this)
        }
    }

    override fun reExecute(): Boolean {
        return false
    }

    override fun onCleared() {
        onDestroy()
    }

    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
        if (response.isSuccessful) {
            if (response.body() != null) {
                setValue(response.body())
            } else {
                setValue(null)
            }
        } else {
            setError(parseError(response))
        }
        for (name: String in response.headers().names()) {
            if (name == ACCESS_TOKEN) {
                Paper.book().write(DB_SESSION_ID, response.headers()[name])
            }
        }
    }

    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
        if (!retry()) {
            setException(t)
        }
    }

    override fun retry(): Boolean {
        return false
    }
}