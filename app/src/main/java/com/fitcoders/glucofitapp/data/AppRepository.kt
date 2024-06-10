package com.fitcoders.glucofitapp.data

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.fitcoders.glucofitapp.response.AssessmentResponse
import com.fitcoders.glucofitapp.response.AssessmentStatusResponse
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.response.LogoutResponse
import com.fitcoders.glucofitapp.response.RegisterResponse
import com.fitcoders.glucofitapp.service.ApiService
import com.fitcoders.glucofitapp.utils.Event
import com.fitcoders.glucofitapp.view.activity.login.LoginActivity
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
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

    private val _assessmentStatus = MutableLiveData<Boolean?>()
    val assessmentStatus: MutableLiveData<Boolean?> = _assessmentStatus


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



    fun checkAssessmentStatus() {
        _isLoading.value = true
        val client = apiService.checkAssessmentStatus()

        client.enqueue(object : Callback<AssessmentStatusResponse> {
            override fun onResponse(
                call: Call<AssessmentStatusResponse>,
                response: Response<AssessmentStatusResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val hasAssessment = response.body()?.hasAssessment
                    if (_assessmentStatus.value != hasAssessment) { // Pastikan hanya diperbarui jika nilainya berbeda
                        Log.d("AppRepository", "API Response: hasAssessment = $hasAssessment")
                        _assessmentStatus.value = hasAssessment
                    }
                } else {
                    _toastText.value = Event("Failed to check assessment status.")
                }
            }

            override fun onFailure(call: Call<AssessmentStatusResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failure: ${t.message}")
            }
        })
    }

    fun postAssessment(
    name: String, dob: String, gender: String, weight: String, height: String,
    historyOfDiabetes: String, familyHistoryOfDiabetes: String,
    sweetConsumption: String, sugarIntake: String,
    exerciseFrequency: String, foodPreferences: String,
    foodAllergies: String, foodLikes: String, foodDislikes: String
    ) {
        val tag = "PostAssessment" // Nama tag untuk log
        _isLoading.value = true

        // Log input parameters
        Log.d(tag, "Input Parameters - Name: $name, DOB: $dob, Gender: $gender, Weight: $weight, Height: $height")
        Log.d(tag, "History: Diabetes: $historyOfDiabetes, Family: $familyHistoryOfDiabetes")
        Log.d(tag, "Consumption: Sweet: $sweetConsumption, Sugar: $sugarIntake")
        Log.d(tag, "Exercise: Frequency: $exerciseFrequency")
        Log.d(tag, "Preferences: Food: $foodPreferences, Allergies: $foodAllergies, Likes: $foodLikes, Dislikes: $foodDislikes")

        val client = apiService.postAssessment(
            name, dob, gender, weight, height,
            historyOfDiabetes, familyHistoryOfDiabetes,
            sweetConsumption, sugarIntake,
            exerciseFrequency, foodPreferences,
            foodAllergies, foodLikes, foodDislikes
        )

        client.enqueue(object : Callback<AssessmentResponse> {
            override fun onResponse(call: Call<AssessmentResponse>, response: Response<AssessmentResponse>) {
                _isLoading.value = false
                // Log the response code and message
                Log.d(tag, "Response Code: ${response.code()}")
                Log.d(tag, "Response Message: ${response.message()}")

                if (response.isSuccessful) {
                    _assessmentStatus.value = true
                    _toastText.value = Event("Assessment Submitted Successfully")
                    // Log the successful response body
                    response.body()?.let {
                        Log.d(tag, "Response Body: $it")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _toastText.value = Event("Error: ${response.code()} ${response.message()}. Body: $errorBody")
                    // Log the error response body
                    Log.e(tag, "Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<AssessmentResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failure: ${t.message}")
                // Log the failure message
                Log.e(tag, "Failure: ${t.message}")
            }
        })
    }

    fun logout(callback: (Boolean, String?) -> Unit) {
        val call = apiService.logout()
        call.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    // Menghapus data sesi pengguna setelah logout berhasil
                    runBlocking {
                        pref.logout()
                    }
                    callback(true, response.body()?.message)
                } else {
                    callback(false, "Logout failed")
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                callback(false, t.message)
            }
        })
    }




    fun getSession(): LiveData<UserModel> {
        return pref.getSession().asLiveData()
    }

    suspend fun saveSession(session: UserModel) {
        pref.saveSession(session)
    }

    suspend fun login() {
        pref.login()
    }

   /* suspend fun logout() {
        pref.logout()
        Log.d("AppRepository", "Logout called")
    }*/

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


