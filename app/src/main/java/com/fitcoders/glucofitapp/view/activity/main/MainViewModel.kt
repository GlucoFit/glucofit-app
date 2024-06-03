package com.fitcoders.glucofitapp.view.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.utils.Event

class MainViewModel (private val repository: AppRepository) : ViewModel() {

    val toastText: LiveData<Event<String>> = repository.toastText

    fun getSession(): LiveData<UserModel> {
        return repository.getSession()
    }
}