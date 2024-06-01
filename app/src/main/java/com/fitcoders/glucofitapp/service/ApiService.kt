package com.fitcoders.glucofitapp.service

import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun postRegister(
        @Field("userName") userName: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>



}