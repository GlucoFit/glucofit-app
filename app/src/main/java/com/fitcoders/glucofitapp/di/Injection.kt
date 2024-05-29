package com.fitcoders.glucofitapp.di

import android.content.Context
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserPreference
import com.fitcoders.glucofitapp.data.dataStore
import com.fitcoders.glucofitapp.service.ApiConfig


object Injection {

    fun provideRepository(context: Context): AppRepository {
        val preferences = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return AppRepository.getInstance(preferences, apiService)
    }
}