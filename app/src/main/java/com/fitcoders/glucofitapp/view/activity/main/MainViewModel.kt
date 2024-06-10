package com.fitcoders.glucofitapp.view.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.launch

class MainViewModel (private val repository: AppRepository) : ViewModel() {

    val toastText: LiveData<Event<String>> = repository.toastText

    fun getSession(): LiveData<UserModel> {
        return repository.getSession()
    }
}