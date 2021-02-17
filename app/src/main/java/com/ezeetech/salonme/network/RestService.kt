/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network

import com.ezeetech.salonme.network.model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface RestService {

    @POST("api_user_login")
    fun signIn(@Body signInRequest: SignInRequest): Call<SignInResponse>

    @GET("api_get_sliders")
    fun getSliders(): Call<SlidersResponse>

    @POST("registry/users/login/renew")
    fun renew(@Body signInRequest: SignInRequest): Call<SignInResponse>

    @POST("api_user_register")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    /*@POST("registry/users/login/renew")
    fun renew(@Body signInRequest: SignInRequest): Call<SignInResponse>

    @POST("registry/pin/new")
    fun forgotPin(@Body jsonObject: JsonObject): Call<Void>

    @POST("users/pin")
    fun validateNewPin(@Body jsonObject: JsonObject): Call<Void>

    @POST("registry/otp/new")
    fun requestOTP(@Body jsonObject: JsonObject): Call<Void>

    @PUT("registry/otp")
    fun changeMobile(@Body jsonObject: JsonObject): Call<Void>

    @POST("users/otp")
    fun validateCode(@Body jsonObject: JsonObject): Call<Void>

    @PUT("users")
    fun saveUser(@Body userRequest: UserRequest): Call<Void>

    @GET("users")
    fun getUser(): Call<UserResponse>

    @GET("users/images")
    fun getImage(): Call<ResponseBody>

    @Multipart
    @PUT("users/images")
    fun saveImage(@Part file: MultipartBody.Part): Call<Void>

    @DELETE("users/images")
    fun deleteImage(): Call<Void>

    @PUT("users/pin")
    fun changePIN(@Body jsonObject: JsonObject): Call<Void>*/

}