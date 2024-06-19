package com.fitcoders.glucofitapp.view.activity.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(private val repository: AppRepository) : ViewModel() {

    val loginResponse: LiveData<LoginResponse> = repository.loginResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: MutableLiveData<Event<String>> = repository.toastText
    val assessmentStatus: MutableLiveData<Boolean?> = repository.assessmentStatus


    fun postLogin(email: String, password: String) {
        viewModelScope.launch {
            repository.postLogin(email, password)
        }
    }

    fun saveSession(session: UserModel) {
        viewModelScope.launch {
            repository.saveSession(session)
        }
    }



    fun checkAssessmentStatus() {
        viewModelScope.launch {
            repository.checkAssessmentStatus()
        }
    }


}
