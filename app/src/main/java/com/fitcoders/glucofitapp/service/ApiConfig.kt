package com.fitcoders.glucofitapp.service

import android.content.Context
import android.util.Log
import com.fitcoders.glucofitapp.data.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        fun getApiService(preferences: UserPreference): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(AuthInterceptor(preferences))
                .connectTimeout(30, TimeUnit.SECONDS)  // Increased timeout
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

            val client = clientBuilder.build()

            val retrofit = Retrofit.Builder()
                //.baseUrl("https://glucofit-api-public-l76ziq6bya-et.a.run.app/api/")
                .baseUrl("http://195.35.6.208:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

// Interceptor untuk menambahkan Header Authorization
class AuthInterceptor(private val preferences: UserPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking{
        val token: String = preferences.getToken()

        Log.i("TOKEN", "intercept: $token")
        val request = chain.request().newBuilder()

        // Only add the Authorization header if the token is not empty
        if (token.isNotEmpty()) {
            request.addHeader("Authorization", "Bearer $token")
        } else {
            Log.i("TOKEN", "No token available, proceeding without Authorization header")
        }

        chain.proceed(request.build())
    }
}

