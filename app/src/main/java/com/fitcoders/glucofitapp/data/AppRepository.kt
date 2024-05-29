package com.fitcoders.glucofitapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fitcoders.glucofitapp.response.RegisterResponse
import com.fitcoders.glucofitapp.service.ApiService
import com.fitcoders.glucofitapp.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository private constructor(private val pref: UserPreference, private val apiService: ApiService){
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun pRegister(username: String, email: String, password: String) {
        _isLoading.value = true
        val client = apiService.Postregister(username, email, password)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        _registerResponse.value = registerResponse!!
                        _toastText.value = Event(registerResponse.message ?: "Success")
                    } else {
                        _toastText.value = Event("Response body is empty.")
                    }
                } else {
                    _toastText.value = Event("Error: ${response.code()} ${response.message()}")
                    Log.e(TAG, "onResponse error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failure: ${t.message}")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "AppRepository"

        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(preferences, apiService)
            }.also { instance = it }

    }
}