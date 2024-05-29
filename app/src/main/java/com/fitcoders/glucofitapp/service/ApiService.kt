package com.fitcoders.glucofitapp.service

import com.fitcoders.glucofitapp.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun Postregister(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

}