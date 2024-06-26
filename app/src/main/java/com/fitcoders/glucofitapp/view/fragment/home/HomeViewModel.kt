package com.fitcoders.glucofitapp.view.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.response.DataItem
import com.fitcoders.glucofitapp.response.FavoritResponse
import com.fitcoders.glucofitapp.response.FoodRecipeResponseItem
import com.fitcoders.glucofitapp.response.GetAssesmantResponse
import com.fitcoders.glucofitapp.response.GetUserResponse
import com.fitcoders.glucofitapp.response.RecommendationResponseItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeViewModel(private val repository: AppRepository) : ViewModel() {

    private val _todaySugarIntake = MutableLiveData<Int>()
    val todaySugarIntake: LiveData<Int> get() = _todaySugarIntake

    val recommendationResponse: LiveData<Result<List<RecommendationResponseItem>>>
        get() = repository.recommendationResponse

    val userResponse1: LiveData<GetUserResponse?> get() = repository.userResponse

    val favoriteResponse: LiveData<FavoritResponse?> get() = repository.favoriteResponse

    val resultResponse: LiveData<GetAssesmantResponse?> = repository.assessmentResponse

    fun fetchTodaySugarIntake() {
        viewModelScope.launch {
            val todayDate = getCurrentDate()
            repository.fetchScanHistoryByDate(todayDate)

            // Observe the scanHistoryResponse LiveData
            repository.scanHistoryResponse.observeForever { result ->
                result.onSuccess { dataItems ->
                    val totalSugar = calculateTotalSugar(dataItems)
                    _todaySugarIntake.value = totalSugar
                }.onFailure {
                    _todaySugarIntake.value = 0 // Set default value on failure
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun calculateTotalSugar(dataItems: List<DataItem>): Int {
        return dataItems.sumOf { it.objectSugar ?: 0 }
    }

    fun fetchRecommendations() {
        viewModelScope.launch {
            repository.getRecommendation()
        }
    }

    fun fetchUserData() {
        viewModelScope.launch {
            repository.fetchUserData()
        }
    }

    // Mark an item as favorite
    fun markAsFavorite(foodId: Int, isFavorite: Int) {
        viewModelScope.launch {
            repository.markAsFavorite(foodId, isFavorite)
        }
    }

    fun fetchAssessmentData() {
        viewModelScope.launch {
            repository.fetchAssessments()
        }
    }

}


