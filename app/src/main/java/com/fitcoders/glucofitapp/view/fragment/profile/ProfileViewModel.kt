package com.fitcoders.glucofitapp.view.fragment.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.response.GetUserResponse
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.launch

class ProfileViewModel (private val repository: AppRepository) : ViewModel() {

    // LiveData untuk menampung respons data pengguna dari repository
    val userResponse: LiveData<GetUserResponse?> get() = repository.userResponse
    val isLoading: LiveData<Boolean> get() = repository.isLoading
    val toastText: LiveData<Event<String>> get() = repository.toastText


    fun logout(onLogoutResult: (Boolean, String?) -> Unit) {
        try {
            repository.logout { success, message ->
                onLogoutResult(success, message)
            }
        } catch (e: Exception) {
            // Handle logout error
            Log.e("ProfileViewModel", "Error logging out: ${e.message}")
            onLogoutResult(false, "Error logging out: ${e.message}")
        }
    }

    fun fetchUserData() {
        viewModelScope.launch {
            repository.fetchUserData()
        }
    }

}