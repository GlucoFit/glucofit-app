package com.fitcoders.glucofitapp.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.di.Injection
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentViewModel
import com.fitcoders.glucofitapp.view.activity.login.LoginViewModel
import com.fitcoders.glucofitapp.view.activity.main.MainViewModel
import com.fitcoders.glucofitapp.view.activity.register.RegisterViewModel
import com.fitcoders.glucofitapp.view.fragment.history.HistoryViewModel
import com.fitcoders.glucofitapp.view.fragment.home.HomeViewModel
import com.fitcoders.glucofitapp.view.fragment.profile.ProfileViewModel

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AssessmentViewModel::class.java) -> {
                AssessmentViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
