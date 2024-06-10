package com.fitcoders.glucofitapp.view.fragment.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fitcoders.glucofitapp.data.repository.ScanHistoryRepository
import kotlinx.coroutines.Dispatchers

class HistoryViewModel(private val repo : ScanHistoryRepository) : ViewModel() {
    fun getHistory() = repo.getScanHistory().asLiveData(Dispatchers.IO)
}