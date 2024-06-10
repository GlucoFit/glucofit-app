package com.fitcoders.glucofitapp.data.repository

import com.fitcoders.glucofitapp.data.History
import com.fitcoders.glucofitapp.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ScanHistoryRepository {
    fun getScanHistory() : Flow<ResultWrapper<List<History>>>
}