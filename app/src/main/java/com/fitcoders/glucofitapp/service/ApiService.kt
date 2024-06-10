package com.fitcoders.glucofitapp.service


import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.response.AssessmentResponse
import com.fitcoders.glucofitapp.response.AssessmentStatusResponse
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.response.LogoutResponse
import com.fitcoders.glucofitapp.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

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

     @FormUrlEncoded
    @POST("assessments")
    fun postAssessment(
        @Field("q1") name: String,
        @Field("q2") dob: String,
        @Field("q3") gender: String,
        @Field("q4") weight: String,
        @Field("q5") height: String,
        @Field("q6") historyOfDiabetes: String,
        @Field("q7") familyHistoryOfDiabetes: String,
        @Field("q8") sweetConsumption: String,
        @Field("q9") sugarIntake: String,
        @Field("q10") exerciseFrequency: String,
        @Field("q11") foodPreferences: String,
        @Field("q12") foodAllergies: String,
        @Field("q13") foodLikes: String,
        @Field("q14") foodDislikes: String
    ): Call<AssessmentResponse>

    @POST("auth/logout")
    fun logout(
   ): Call<LogoutResponse>



}


