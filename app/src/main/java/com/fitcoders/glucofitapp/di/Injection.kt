package com.fitcoders.glucofitapp.di

import android.content.Context
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserPreference
import com.fitcoders.glucofitapp.data.dataStore
import com.fitcoders.glucofitapp.service.ApiConfig
import com.fitcoders.glucofitapp.service.ApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect


object Injection {

    fun provideRepository(context: Context): AppRepository {
        val preferences = UserPreference.getInstance(context.dataStore)
       val apiService = ApiConfig.getApiService(preferences)
        return AppRepository.getInstance(preferences, apiService)
    }

}