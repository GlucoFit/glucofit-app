package com.fitcoders.glucofitapp.view.fragment.profile

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

class ProfileViewModel (private val repository: AppRepository) : ViewModel() {


    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> get() = _logoutStatus

    private val _logoutMessage = MutableLiveData<String?>()
    val logoutMessage: MutableLiveData<String?> get() = _logoutMessage

    fun logout(onLogoutResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            repository.logout { success, message ->
                onLogoutResult(success, message)
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession()
    }


}