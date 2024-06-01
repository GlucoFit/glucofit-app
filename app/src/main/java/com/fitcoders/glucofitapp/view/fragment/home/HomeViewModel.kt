package com.fitcoders.glucofitapp.view.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserModel

class HomeViewModel(private val repository: AppRepository) : ViewModel() {
    // TODO: Implement the ViewModel

    fun getSession(): LiveData<UserModel> {
        return repository.getSession()
    }
}