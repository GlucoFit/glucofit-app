package com.fitcoders.glucofitapp.data.datasource.history

import com.fitcoders.glucofitapp.service.ApiService
import com.fitcoders.glucofitapp.service.network.model.HistoryResponse

interface ScanHistoryApiDataSource{
    suspend fun getAllHistoryData() : HistoryResponse

}