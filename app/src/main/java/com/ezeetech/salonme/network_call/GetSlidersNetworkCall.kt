package com.ezeetech.salonme.network_call


import android.app.Application
import com.ezeetech.salonme.network.model.NetworkModelWithSession
import com.ezeetech.salonme.network.model.SlidersResponse
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.salonme.base.DB_SLIDERS
import com.salonme.base.DB_USER
import com.salonme.base.Trace
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetSlidersNetworkCall(application: Application) :
    NetworkModelWithSession<Boolean, Nothing?, SlidersResponse>(application),
    Callback<SlidersResponse> {
    override fun execute(req: Nothing?) {
        onDestroy()
        restServices.getSliders().let {
            call = it
            it.enqueue(this)
        }
    }

    override fun reExecute(): Boolean {
        getClone()?.let {
            it.enqueue(this)
            return true
        }
        return false
    }

    override fun onCleared() {
        onDestroy()
    }

    override fun onResponse(call: Call<SlidersResponse>, response: Response<SlidersResponse>) {
        if (response.isSuccessful) {
            if(response.body() != null && response.body()!!.success) {
                Trace.i("Sliders res: " + response.body())
                Paper.book().write(DB_SLIDERS, response.body()!!.sliders)
                setValue(true)
            }else{
                setValue(false)
            }
        } else {
            setError(parseError(response))
        }
    }

    private fun result() {
        GlobalScope.launch(Dispatchers.Main) {
            setValue(true)
        }
    }

    override fun onFailure(call: Call<SlidersResponse>, t: Throwable) {
        if (!retry()) {
            setException(t)
        }
    }

    override fun retry(): Boolean {
        return false
    }
}