package com.fitcoders.glucofitapp.view.activity.profile.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.response.*
import kotlinx.coroutines.launch

class AccountViewModel (private val repository: AppRepository) : ViewModel() {

    // LiveData untuk respons pembaruan username
    val updateUsernameResponse: LiveData<GetUserResponse?> = repository.updateUsernameResponse

    // LiveData untuk respons pembaruan email
    val updateEmailResponse: LiveData<GetUserResponse?> = repository.updateEmailResponse


    // LiveData untuk respons penghapusan pengguna
    val deleteUserResponse: LiveData<DeleteResponse?> = repository.deleteUserResponse

    // Fungsi untuk memperbarui email
    fun updateEmail(email: String) {
        viewModelScope.launch {
            repository.updateEmail(email)
        }
    }

    // Fungsi untuk memperbarui username
    fun updateUsername(userName: String) {
        viewModelScope.launch {
            repository.updateUsername(userName)
        }
    }

    // Fungsi untuk memperbarui password


    // Fungsi untuk menghapus pengguna
    fun deleteUser() {
        viewModelScope.launch {
            repository.deleteUser()
        }
    }
}

