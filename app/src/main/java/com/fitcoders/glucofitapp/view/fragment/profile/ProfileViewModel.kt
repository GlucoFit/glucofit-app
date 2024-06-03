package com.fitcoders.glucofitapp.view.fragment.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.launch

class ProfileViewModel (private val repository: AppRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            Log.d("ProfileViewModel", "Logout executed in ViewModel")
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession()
    }
}