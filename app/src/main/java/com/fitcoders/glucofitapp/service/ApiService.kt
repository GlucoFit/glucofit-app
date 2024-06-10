package com.fitcoders.glucofitapp.service

import android.util.Log
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.response.AssessmentResponse
import com.fitcoders.glucofitapp.response.AssessmentStatusResponse
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.response.LogoutResponse
import com.fitcoders.glucofitapp.response.RegisterResponse
import com.fitcoders.glucofitapp.response.ScanHistoryResponse
import com.fitcoders.glucofitapp.service.network.model.HistoryResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

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

    @GET("scan/history")
    suspend fun getScanHistory(
    ): HistoryResponse

    companion object {
        @JvmStatic
        operator fun invoke(): ApiService {
            val logging =
                HttpLoggingInterceptor { message -> Log.d("Http-Logging", "log: $message") }
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        val original: Request = chain.request()
                        val requestBuilder: Request.Builder =
                            original.newBuilder()
                                .addHeader("accept", "application/json")
                                .addHeader("Authorization", "Bearer ${"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjksInVzZXJOYW1lIjoidXNlcjIiLCJlbWFpbCI6InVzZXIyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE4MDE3NDI2fQ.CXgdewKaMsNkUYGGwfavHhmBVRc0OKB29nXkLbUbLWU"}")
                        val request: Request = requestBuilder.build()
                        chain.proceed(request)
                    }
                    .addInterceptor(logging)
                    .build()

            val retrofit =
                Retrofit.Builder()
                    .baseUrl("http://195.35.6.208:8080/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }
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


