/*
 *  Created by Vinay on 26-1-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network_call

import android.app.Application
import com.ezeetech.salonme.network.model.NetworkModelWithNoSession
import com.ezeetech.salonme.network.model.NetworkModelWithSession
import com.ezeetech.salonme.network.model.SignInRequest
import com.ezeetech.salonme.network.model.SignInResponse
import com.salonme.base.*
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Todo change extended class with NetworkModelWithNoSession
class SignInNetworkCall(application: Application) :
    NetworkModelWithSession<SignInResponse, SignInRequest, SignInResponse>(application),
    Callback<SignInResponse> {
    override fun execute(req: SignInRequest) {
        onDestroy()
        restServices.signIn(req).let {
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

    override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
        if (response.isSuccessful) {
            if (response.body() != null) {
                if(response.body()!!.success){
                    Preference.instance.putBoolean(PREF_PROFILE_ACTIVE, true)
                    Paper.book().write(DB_SESSION_ID, response.body()!!.accessToken)
                    Paper.book().write(DB_USER, response.body()!!.userData)
                }
                setValue(response.body())
            } else {
                setValue(null)
            }
        } else {
            setError(parseError(response))
        }
        /*for (name: String in response.headers().names()) {
            if (name == ACCESS_TOKEN) {
                Paper.book().write(DB_SESSION_ID, response.headers()[name])
            }
        }*/
    }

    override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
        if (!retry()) {
            setException(t)
        }
    }

    override fun retry(): Boolean {
        return false
    }
}