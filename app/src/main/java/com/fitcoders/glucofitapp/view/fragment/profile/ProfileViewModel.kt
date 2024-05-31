package com.fitcoders.glucofitapp.view.fragment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import kotlinx.coroutines.launch

class ProfileViewModel (private val repository: AppRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}