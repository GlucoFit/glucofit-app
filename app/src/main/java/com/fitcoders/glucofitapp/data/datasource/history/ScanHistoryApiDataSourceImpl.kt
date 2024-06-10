package com.fitcoders.glucofitapp.data.datasource.history

import com.fitcoders.glucofitapp.service.ApiService
import com.fitcoders.glucofitapp.service.network.model.HistoryResponse

class ScanHistoryApiDataSourceImpl(private val service : ApiService) : ScanHistoryApiDataSource {
    override suspend fun getAllHistoryData(): HistoryResponse {
        return service.getScanHistory()
    }
}