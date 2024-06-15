package com.fitcoders.glucofitapp.view.activity.profile.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.response.GetUserResponse
import kotlinx.coroutines.launch

class PasswordViewModel (private val repository: AppRepository) : ViewModel() {

    // LiveData untuk respons pembaruan password
    val updatePasswordResponse: LiveData<GetUserResponse?> = repository.updatePasswordResponse

    fun updatePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            repository.updatePassword(newPassword)
        }
    }
}