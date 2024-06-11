package com.fitcoders.glucofitapp.view.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.response.DataItem
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: AppRepository) : ViewModel() {


    val scanHistoryResponse: LiveData<Result<List<DataItem>>> get() = repository.scanHistoryResponse

    // Method to trigger fetching scan history
    /*fun fetchScanHistory() {
        viewModelScope.launch {
            repository.fetchScanHistory()
        }
    }*/

    fun fetchScanHistoryByDate(date: String) {
        viewModelScope.launch {
            repository.fetchScanHistoryByDate(date)
        }
    }

    // Method untuk menghitung total objectSugar
    fun calculateTotalSugar(data: List<DataItem>): Int {
        return data.sumOf { it.objectSugar ?: 0 }
    }


}