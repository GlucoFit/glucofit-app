package com.fitcoders.glucofitapp.service

import com.fitcoders.glucofitapp.response.HistoryScanResponse
import com.fitcoders.glucofitapp.response.AssessmentResponse
import com.fitcoders.glucofitapp.response.AssessmentStatusResponse
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.response.LogoutResponse
import com.fitcoders.glucofitapp.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    @GET("assessments/status")
    fun checkAssessmentStatus(
    ): Call<AssessmentStatusResponse>

    @GET("scan/history")
    fun getScanHistory(
    ): Call<HistoryScanResponse>

     @FormUrlEncoded
    @POST("assessments")
    fun postAssessment(
        @Field("name") name: String,
        @Field("dob") dob: String,
        @Field("gender") gender: String,
        @Field("weight") weight: String,
        @Field("height") height: String,
        @Field("historyOfDiabetes") historyOfDiabetes: String,
        @Field("familyHistoryOfDiabetes") familyHistoryOfDiabetes: String,
        @Field("sweetConsumption") sweetConsumption: String,
        @Field("sugarIntake") sugarIntake: String,
        @Field("exerciseFrequency") exerciseFrequency: String,
        @Field("foodPreferences") foodPreferences: String,
        @Field("foodAllergies") foodAllergies: String,
        @Field("foodLikes") foodLikes: String,
        @Field("foodDislikes") foodDislikes: String
    ): Call<AssessmentResponse>

    @POST("auth/logout")
    fun logout(
   ): Call<LogoutResponse>



}


