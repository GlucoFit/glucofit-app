package com.fitcoders.glucofitapp.view.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.UserModel
import com.fitcoders.glucofitapp.response.DataItem
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



    fun getSession(): LiveData<UserModel> {
        return repository.getSession()
    }

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
}


