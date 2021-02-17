/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network.model

import android.app.Application
import androidx.lifecycle.*
import com.ezeetech.salonme.network.RestService
import com.ezeetech.salonme.network.RetrofitService
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import java.util.*

abstract class NetworkModel<Type : Any, Req, Res>(application: Application, session: Boolean, vararg accept: String) :
    AndroidViewModel(application) {
    protected lateinit var restServices: RestService
    protected var call: Call<Res>? = null
    private val result: MutableLiveData<NetworkResponse<Type>>
    private var retryCount = 0

    init {
        if (!::restServices.isInitialized) {
            restServices = RetrofitService.getRestService(session, *accept).create(RestService::class.java)
        }
        result = MutableLiveData()
    }

    abstract fun execute(req: Req)
    abstract fun reExecute(): Boolean
    abstract fun retry(): Boolean

    fun getResult(): LiveData<NetworkResponse<Type>> {
        return result
    }

    fun setException(t: Throwable?) {
        result.value = NetworkResponse.Exception(t)
    }

    fun setError(err: List<NetworkError>) {
        result.value = NetworkResponse.Error(err)
    }

    fun setValue(type: Type?) {
        result.value = NetworkResponse.Success(type)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        call?.cancel()
        call = null
    }

    fun getClone(): Call<Res>? {
        return call?.clone()
    }

    fun resetCount() {
        retryCount = 0
    }

    fun reset() {
        result.value = null
    }

    fun parseError(response: Response<*>): List<NetworkError> {
        val type = object : TypeToken<List<NetworkError>>() {
        }.type
        val converter = RetrofitService.getRestService(false)
            .responseBodyConverter<List<NetworkError>>(type, arrayOfNulls<Annotation>(0))
        val networkError: ArrayList<NetworkError> = ArrayList()
        try {
            response.errorBody()?.let { rb ->
                converter.convert(rb)?.let { errorList ->
                    networkError.addAll(errorList)
                }
            }
        } catch (e: Exception) {
        }
        return networkError
    }
}