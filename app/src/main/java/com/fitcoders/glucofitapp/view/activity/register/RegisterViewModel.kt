package com.fitcoders.glucofitapp.view.activity.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.response.RegisterResponse
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.launch

class RegisterViewModel (private val repository: AppRepository): ViewModel() {
    val registerResponse: LiveData<RegisterResponse> = repository.registerResponse
    val toastText: LiveData<Event<String>> = repository.toastText
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun postRegister(username: String, email: String, password: String) {
        viewModelScope.launch {
            repository.pRegister(username, email, password)
        }
    }
}