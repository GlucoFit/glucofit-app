package com.fitcoders.glucofitapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.response.RegisterResponse
import com.fitcoders.glucofitapp.service.ApiService
import com.fitcoders.glucofitapp.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository private constructor(private val pref: UserPreference, private val apiService: ApiService) {

    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: MutableLiveData<RegisterResponse?> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    private val _alertDialog = MutableLiveData<Boolean>()
    val alertDialog: LiveData<Boolean> = _alertDialog

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userProfile = MutableLiveData<LoginResponse>()
    val userProfile: LiveData<LoginResponse> = _userProfile

    fun pRegister(userName: String, email: String, password: String) {
        _isLoading.value = true
        val client = apiService.postRegister(userName, email, password)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        _registerResponse.value = registerResponse
                        _toastText.value = Event("Success Register")
                    } else {
                        _toastText.value = Event("Response body is empty.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _toastText.value = Event("Error: ${response.code()} ${response.message()}. Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failure: ${t.message}")
            }
        })
    }

    fun postLogin(email: String, password: String) {
        _isLoading.value = true
        val client = apiService.postLogin(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _loginResponse.value = response.body()
                    _alertDialog.value = true
                    _toastText.value = Event("Success Login")
                } else {
                    _toastText.value = Event("Response body is empty.")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _toastText.value = Event(t.message.toString())
            }
        })
    }

    /*fun fetchUserProfile(token: String) {
        _isLoading.value = true

        val client = apiService.getUserProfile("$token")

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _userProfile.value = response.body()
                    _toastText.value = Event("Success to fetch user profile.")
                } else {
                    _toastText.value = Event("Failed to fetch user profile: ${response.errorBody()?.string() ?: "Unknown error"}")
                    val errorBody = response.errorBody()?.string()
                    Log.e(
                        TAG,
                        "Error: ${response.code()} ${response.message()}. Body: ${errorBody}"
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Network error: ${t.localizedMessage ?: "Unknown error"}")
                Log.e(TAG, "Network error", t)
            }
        })
    }*/


    fun getSession(): LiveData<UserModel> {
        return pref.getSession().asLiveData()
    }

    suspend fun saveSession(session: UserModel) {
        pref.saveSession(session)
    }

    suspend fun login() {
        pref.login()
    }

    suspend fun logout() {
        pref.logout()
    }



    companion object {
        private const val TAG = "AppRepository"

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(preferences: UserPreference, apiService: ApiService): AppRepository {
            return instance ?: synchronized(this) {
                instance ?: AppRepository(preferences, apiService).also { instance = it }
            }
        }
    }
}

