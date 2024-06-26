package com.fitcoders.glucofitapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.fitcoders.glucofitapp.response.AnalyzeResponse
import com.fitcoders.glucofitapp.response.AssessmentResponse
import com.fitcoders.glucofitapp.response.AssessmentStatusResponse
import com.fitcoders.glucofitapp.response.Data

import com.fitcoders.glucofitapp.response.DataItem
import com.fitcoders.glucofitapp.response.DeleteResponse
import com.fitcoders.glucofitapp.response.FavoritFoodResponseItem
import com.fitcoders.glucofitapp.response.FavoritResponse
import com.fitcoders.glucofitapp.response.FoodRecipeResponseItem
import com.fitcoders.glucofitapp.response.GetAssesmantResponse
import com.fitcoders.glucofitapp.response.GetUserResponse
import com.fitcoders.glucofitapp.response.HistoryScanResponse
import com.fitcoders.glucofitapp.response.LoginResponse
import com.fitcoders.glucofitapp.response.LogoutResponse
import com.fitcoders.glucofitapp.response.RecommendationResponseItem
import com.fitcoders.glucofitapp.response.RegisterResponse
import com.fitcoders.glucofitapp.response.SearchHistoryResponseItem
import com.fitcoders.glucofitapp.response.UplodScanResponse
import com.fitcoders.glucofitapp.service.ApiService
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class AppRepository private constructor(private val pref: UserPreference, private val apiService: ApiService) {

    private val _registerResponse = MutableLiveData<RegisterResponse?>()
    val registerResponse: MutableLiveData<RegisterResponse?> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse:  MutableLiveData<LoginResponse> = _loginResponse

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: MutableLiveData<Event<String>> = _toastText

    private val _alertDialog = MutableLiveData<Boolean>()
    val alertDialog: LiveData<Boolean> = _alertDialog

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _assessmentStatus = MutableLiveData<Boolean?>()
    val assessmentStatus: MutableLiveData<Boolean?> = _assessmentStatus

    private val _scanHistoryResponse = MutableLiveData<Result<List<DataItem>>>()
    val scanHistoryResponse: LiveData<Result<List<DataItem>>> = _scanHistoryResponse

    private val _recommendationResponse = MutableLiveData<Result<List<RecommendationResponseItem>>>()
    val recommendationResponse: LiveData<Result<List<RecommendationResponseItem>>> = _recommendationResponse

    private val _assessmentResponse = MutableLiveData<GetAssesmantResponse?>()
    val assessmentResponse: LiveData<GetAssesmantResponse?> = _assessmentResponse

    private val _foodInfo = MutableLiveData<Data?>()
    val foodInfo: LiveData<Data?> = _foodInfo

    private val _deleteResponse = MutableLiveData<DeleteResponse?>()
    val deleteResponse: LiveData<DeleteResponse?> = _deleteResponse

    private val _userResponse = MutableLiveData<GetUserResponse?>()
    val userResponse: LiveData<GetUserResponse?> = _userResponse

    private val _updateEmailResponse = MutableLiveData<GetUserResponse?>()
    val updateEmailResponse: LiveData<GetUserResponse?> = _updateEmailResponse

    private val _updateUsernameResponse = MutableLiveData<GetUserResponse?>()
    val updateUsernameResponse: LiveData<GetUserResponse?> = _updateUsernameResponse

    private val _updatePasswordResponse = MutableLiveData<GetUserResponse?>()
    val updatePasswordResponse: LiveData<GetUserResponse?> = _updatePasswordResponse

    private val _deleteUserResponse = MutableLiveData<DeleteResponse?>()
    val deleteUserResponse: LiveData<DeleteResponse?> = _deleteUserResponse

    // LiveData untuk menyimpan hasil pencarian makanan
    private val _searchResults = MutableLiveData<Result<List<FoodRecipeResponseItem>>>()
    val searchResults: LiveData<Result<List<FoodRecipeResponseItem>>> = _searchResults

    // LiveData untuk menyimpan riwayat pencarian
    private val _searchHistory = MutableLiveData<Result<List<SearchHistoryResponseItem>>>()
    val searchHistory: LiveData<Result<List<SearchHistoryResponseItem>>> = _searchHistory

    private val _favoriteResponse = MutableLiveData<FavoritResponse?>()
    val favoriteResponse: LiveData<FavoritResponse?> = _favoriteResponse

    private val _favoriteFoods = MutableLiveData<Result<List<FavoritFoodResponseItem>>>()
    val favoriteFoods: MutableLiveData<Result<List<FavoritFoodResponseItem>>> get() = _favoriteFoods

    private val _uploadScanResponse = MutableLiveData<UplodScanResponse?>()
    val uploadScanResponse: LiveData<UplodScanResponse?> get() = _uploadScanResponse


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


    // Method untuk mengambil data scan history berdasarkan tanggal
    fun fetchScanHistoryByDate(date: String) {
        Log.d("AppRepository", "Fetching scan history for date: $date")
        _isLoading.value = true
        val client = apiService.getScanHistory()

        client.enqueue(object : Callback<HistoryScanResponse> {
            override fun onResponse(
                call: Call<HistoryScanResponse>,
                response: Response<HistoryScanResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val scanHistory = response.body()?.data ?: emptyList()
                    // Log contoh createdAt
                    scanHistory.forEach { item ->
                        Log.d("AppRepository", "CreatedAt: ${item?.createdAt}")
                    }
                    // Filter data berdasarkan tanggal yang diberikan
                    val filteredHistory = scanHistory.filter { item ->
                        // Konversi createdAt dari UTC ke waktu lokal
                        val localCreatedAt = item?.createdAt?.let { convertUtcToLocalTime(it) }
                        // Log konversi waktu lokal
                        Log.d("AppRepository", "Local CreatedAt: $localCreatedAt")
                        // Filter berdasarkan tanggal lokal yang diberikan
                        localCreatedAt?.startsWith(date) == true
                    }
                    Log.d("AppRepository", "Filtered history size: ${filteredHistory.size}")
                    _scanHistoryResponse.value = Result.success(filteredHistory.filterNotNull())
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AppRepository", "Error fetching history: ${response.code()} - ${response.message()}, Body: $errorBody")
                    _toastText.value = Event("Error: ${response.code()} ${response.message()}. Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<HistoryScanResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("AppRepository", "Failure fetching history: ${t.message}")
                _scanHistoryResponse.value = Result.failure(t)
                _toastText.value = Event("Failure: ${t.message}")
            }
        })
    }

    // Fungsi untuk mengonversi waktu dari UTC ke waktu lokal
    private fun convertUtcToLocalTime(utcTime: String): String {
        // Format waktu yang diberikan dalam UTC
        val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Parse waktu dalam UTC
        val date: Date? = utcFormat.parse(utcTime)

        // Format waktu untuk zona waktu lokal
        val localFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Gunakan format tanggal saja untuk pencocokan
        localFormat.timeZone = TimeZone.getDefault() // Set ke zona waktu lokal perangkat

        // Konversi dan kembalikan waktu dalam format zona waktu lokal
        return date?.let { localFormat.format(it) } ?: utcTime
    }



    // Fungsi untuk memanggil API berdasarkan label makanan
    /*fun fetchFoodInfoByLabel(label: String) {
        _isLoading.value = true
        val call = apiService.getFoodInfoByLabel(label)
        call.enqueue(object : Callback<DataFoodResponse> {
            override fun onResponse(call: Call<DataFoodResponse>, response: Response<DataFoodResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _foodInfo.postValue(response.body())
                } else {
                    _foodInfo.postValue(null) // Null atau data default jika gagal
                    _toastText.postValue(Event("Failed to fetch food info: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<DataFoodResponse>, t: Throwable) {
                _isLoading.value = false
                _foodInfo.postValue(null) // Null atau data default jika gagal
                _toastText.postValue(Event("API call failed: ${t.message}"))
            }
        })
    }*/

    fun deleteScanHistoryById(id: Int) {
        _isLoading.value = true
        val call = apiService.deleteScanHistoryById(id)

        call.enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _deleteResponse.value = response.body()
                    _toastText.value = Event("Scan history deleted successfully")
                    Log.d(TAG, "Deleted scan history with ID: $id")
                } else {
                    val errorBody = response.errorBody()?.string()
                    _toastText.value = Event("Failed to delete scan history: ${response.code()} ${response.message()}. Body: $errorBody")
                    Log.e(TAG, "Error deleting scan history: ${response.code()} - ${response.message()}, Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failure: ${t.message}")
                Log.e(TAG, "Failure deleting scan history: ${t.message}")
            }
        })
    }


    fun getRecommendation() {
        _isLoading.value = true
        val call = apiService.getRecommendations()

        Log.d(TAG, "Starting API call to get recommendations")

        call.enqueue(object : Callback<List<RecommendationResponseItem>> {
            override fun onResponse(
                call: Call<List<RecommendationResponseItem>>,
                response: Response<List<RecommendationResponseItem>>
            ) {
                _isLoading.value = false

                Log.d(TAG, "API Response Code: ${response.code()}")
                Log.d(TAG, "API Response Message: ${response.message()}")

                if (response.isSuccessful) {
                    val recommendations = response.body() ?: emptyList()

                    Log.d(TAG, "Number of recommendations received: ${recommendations.size}")

                    _recommendationResponse.value = Result.success(recommendations.filterNotNull())
                } else {
                    val errorBody = response.errorBody()?.string()

                    Log.e(TAG, "Failed to fetch recommendations. Error code: ${response.code()}")
                    Log.e(TAG, "Error message: ${response.message()}")
                    Log.e(TAG, "Error body: $errorBody")

                    _recommendationResponse.value = Result.failure(Throwable("Failed to fetch recommendations. Error code: ${response.code()}. Message: ${response.message()}. Body: $errorBody"))
                    _toastText.value = Event("Error: ${response.code()} ${response.message()}. Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<List<RecommendationResponseItem>>, t: Throwable) {
                _isLoading.value = false

                Log.e(TAG, "API call to fetch recommendations failed", t)

                _recommendationResponse.value = Result.failure(t)
                _toastText.value = Event("Failure: ${t.message}")
            }
        })
    }

    // Fungsi untuk mengambil makanan favorit dari API
    fun getFavoriteFoods() {
        _isLoading.value = true
        val call = apiService.getFavorite()

        call.enqueue(object : Callback<List<FavoritFoodResponseItem>> {
            override fun onResponse(call: Call<List<FavoritFoodResponseItem>>, response: Response<List<FavoritFoodResponseItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val favoriteItems = response.body() ?: emptyList()
                    Log.d("AppRepository", "Fetched favorite foods successfully. Total items: ${favoriteItems.size}")
                    favoriteItems.forEach { item ->
                        Log.d("AppRepository", "Favorite Item: $item")
                    }
                    _favoriteFoods.value = Result.success(favoriteItems.filterNotNull())
                } else {
                    val errorBody = response.errorBody()?.string()
                    _toastText.value = Event("Failed to fetch favorite foods: ${response.code()} ${response.message()}. Body: $errorBody")
                    Log.e("AppRepository", "Failed to fetch favorite foods. Error: $errorBody")
                }
            }

            override fun onFailure(call: Call<List<FavoritFoodResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("API call failed: ${t.message}")
                Log.e("AppRepository", "Failed to fetch favorite foods. Throwable: ${t.message}")
            }
        })
    }





    //user me
    fun fetchUserData() {
        _isLoading.value = true
        val client = apiService.getUser()

        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userResponse.value = response.body()
                } else {
                    _toastText.value = Event("Failed to fetch user data: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Error: ${t.message}")
            }
        })
    }
    //get assesment
    fun fetchAssessments() {
        _isLoading.value = true
        val client = apiService.getAssessments()

        client.enqueue(object : Callback<GetAssesmantResponse> {
            override fun onResponse(call: Call<GetAssesmantResponse>, response: Response<GetAssesmantResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _assessmentResponse.value = response.body()
                } else {
                    _assessmentResponse.value = null
                    _toastText.value = Event("Failed to fetch assessment data: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetAssesmantResponse>, t: Throwable) {
                _isLoading.value = false
                _assessmentResponse.value = null
                _toastText.value = Event("Error: ${t.message}")
            }
        })
    }

    // Fungsi untuk memperbarui email
    fun updateEmail(email: String) {
        val emailUpdate = mapOf("email" to email)
        val client = apiService.updateEmail(emailUpdate)

        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                if (response.isSuccessful) {
                    _updateEmailResponse.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _updateEmailResponse.value = null
                    Log.e("UpdateEmail", "Error: $errorBody")
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                _updateEmailResponse.value = null
                Log.e("UpdateEmail", "Failure: ${t.message}")
            }
        })
    }

    // Fungsi untuk memperbarui username
    fun updateUsername(userName: String) {
        val usernameUpdate = mapOf("userName" to userName)
        Log.d("UpdateUsername", "Sending payload: $usernameUpdate")

        val client = apiService.updateUsername(usernameUpdate)

        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                Log.d("UpdateUsername", "Response code: ${response.code()} - ${response.message()}")
                if (response.isSuccessful) {
                    _updateUsernameResponse.value = response.body()
                    Log.d("UpdateUsername", "Updated user data: ${response.body()}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UpdateUsername", "Error: $errorBody")
                    _toastText.value = Event("Error: ${response.code()} ${response.message()}. Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                Log.e("UpdateUsername", "Failure: ${t.message}")
                _updateUsernameResponse.value = null
                _toastText.value = Event("Failure: ${t.message}")
            }
        })
    }


    // Fungsi untuk memperbarui password
    fun updatePassword(password: String) {
        val passwordUpdate = mapOf("password" to password)
        val client = apiService.updatePassword(passwordUpdate)

        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                if (response.isSuccessful) {
                    _updatePasswordResponse.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _updatePasswordResponse.value = null
                    Log.e("UpdatePassword", "Error: $errorBody")
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                _updatePasswordResponse.value = null
                Log.e("UpdatePassword", "Failure: ${t.message}")
            }
        })
    }

    // Fungsi untuk menghapus pengguna
    fun deleteUser() {
        val client = apiService.deleteUser()

        client.enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                if (response.isSuccessful) {
                    _deleteUserResponse.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _deleteUserResponse.value = null
                    Log.e("DeleteUser", "Error: $errorBody")
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                _deleteUserResponse.value = null
                Log.e("DeleteUser", "Failure: ${t.message}")
            }
        })
    }

    // Metode untuk mencari makanan berdasarkan nama resep
    fun searchFoodByName(recipeName: String) {
        _isLoading.value = true
        val client = apiService.getFoodByRecipeName(recipeName)

        client.enqueue(object : Callback<List<FoodRecipeResponseItem>> {
            override fun onResponse(call: Call<List<FoodRecipeResponseItem>>, response: Response<List<FoodRecipeResponseItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val foodItems = response.body() ?: emptyList()
                    _searchResults.value = Result.success(foodItems)
                } else {
                    _searchResults.value = Result.failure(Throwable("Failed to fetch food recipes: ${response.message()}"))
                    _toastText.value = Event("Failed to fetch food recipes: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FoodRecipeResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _searchResults.value = Result.failure(t)
                _toastText.value = Event("API call failed: ${t.message}")
            }
        })
    }

    // Metode untuk mendapatkan riwayat pencarian
    fun getSearchHistory() {
        _isLoading.value = true
        val client = apiService.getSearchHistory()

        client.enqueue(object : Callback<List<SearchHistoryResponseItem>> {
            override fun onResponse(call: Call<List<SearchHistoryResponseItem>>, response: Response<List<SearchHistoryResponseItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val historyItems = response.body() ?: emptyList()
                    _searchHistory.value = Result.success(historyItems)
                } else {
                    _searchHistory.value = Result.failure(Throwable("Failed to fetch search history: ${response.message()}"))
                    _toastText.value = Event("Failed to fetch search history: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<SearchHistoryResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _searchHistory.value = Result.failure(t)
                _toastText.value = Event("API call failed: ${t.message}")
            }
        })
    }

    fun markAsFavorite(foodId: Int, isFavorite: Int) {
        _isLoading.value = true

        val call = apiService.markAsFavorite(foodId,isFavorite)

        call.enqueue(object : Callback<FavoritResponse> {
            override fun onResponse(call: Call<FavoritResponse>, response: Response<FavoritResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _favoriteResponse.value = response.body()
                } else {
                    _favoriteResponse.value = null
                }
            }

            override fun onFailure(call: Call<FavoritResponse>, t: Throwable) {
                _isLoading.value = false
                _favoriteResponse.value = null
                _toastText.value = Event("API call failed: ${t.message}")
            }
        })
    }

    fun analyzeFoodImage(image: File) {
        if (!image.exists() || !image.isFile) {
            _toastText.postValue(Event("Invalid image file"))
            return
        }

        _isLoading.value = true
        val requestFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", image.name, requestFile)

        apiService.analyzeFoodImage(multipartBody).enqueue(object : Callback<AnalyzeResponse> {
            override fun onResponse(call: Call<AnalyzeResponse>, response: Response<AnalyzeResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val analyzeResponse = response.body()
                    _foodInfo.value = analyzeResponse?.data

                    // Log untuk memastikan data yang diterima
                    Log.d("ScanViewModel", "AnalyzeResponse received: ${analyzeResponse?.data}")
                } else {
                    _toastText.value = Event("Failed to analyze image")
                    Log.e("ScanViewModel", "Failed to analyze image: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AnalyzeResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("API call failed: ${t.message}")
                Log.e("ScanViewModel", "API call failed", t)
            }
        })
    }

    // Metode untuk mengunggah hasil analisis
    fun uploadScanImage(imageFile: File, objectName: String, objectSugar: String, datasetLabel: String) {
        _isLoading.value = true

        val requestObjectName = RequestBody.create("text/plain".toMediaTypeOrNull(), objectName)
        val requestObjectSugar = RequestBody.create("text/plain".toMediaTypeOrNull(), objectSugar)
        val requestDatasetLabel = RequestBody.create("text/plain".toMediaTypeOrNull(), datasetLabel)

        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
        val multipartImage = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        apiService.uploadScanImage(multipartImage, requestObjectName, requestObjectSugar, requestDatasetLabel)
            .enqueue(object : Callback<UplodScanResponse> {
                override fun onResponse(call: Call<UplodScanResponse>, response: Response<UplodScanResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _uploadScanResponse.value = response.body()
                        Log.d("AppRepository", "UploadScanResponse received: ${response.body()}")
                    } else {
                        _toastText.value = Event("Failed to upload scan: ${response.message()}")
                        Log.e("AppRepository", "Failed to upload scan: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<UplodScanResponse>, t: Throwable) {
                    _isLoading.value = false
                    _toastText.value = Event("API call failed: ${t.message}")
                    Log.e("AppRepository", "API call failed", t)
                }
            })
    }



   /* fun getSession(): LiveData<UserModel> {
        return pref.getSession().asLiveData()
    }*/

    suspend fun saveSession(session: UserModel) {
        pref.saveSession(session)
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


