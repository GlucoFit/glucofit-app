package com.fitcoders.glucofitapp.view.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.response.DataItem
import com.fitcoders.glucofitapp.response.DeleteResponse
import com.fitcoders.glucofitapp.response.GetAssesmantResponse
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: AppRepository) : ViewModel() {

    val resultResponse: LiveData<GetAssesmantResponse?> = repository.assessmentResponse

    val scanHistoryResponse: LiveData<Result<List<DataItem>>> get() = repository.scanHistoryResponse

    val deleteResponse: LiveData<DeleteResponse?> get() = repository.deleteResponse

    private val _todaySugarIntake = MutableLiveData<Int>()
    val todaySugarIntake: LiveData<Int> get() = _todaySugarIntake


    fun fetchScanHistoryByDate(date: String) {
        viewModelScope.launch {
            repository.fetchScanHistoryByDate(date)
        }
    }
    // Method untuk menghitung total objectSugar
    fun calculateTotalSugar(data: List<DataItem>): Int {
        return data.sumOf { it.objectSugar ?: 0 }
    }

    fun deleteScanHistoryById(id: Int) {
        viewModelScope.launch {
            repository.deleteScanHistoryById(id)
        }
    }

    fun fetchAssessmentData() {
        viewModelScope.launch {
            repository.fetchAssessments()
        }
    }



}