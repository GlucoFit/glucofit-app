package com.fitcoders.glucofitapp.data.repository

import com.fitcoders.glucofitapp.data.History
import com.fitcoders.glucofitapp.data.datasource.history.ScanHistoryApiDataSource
import com.fitcoders.glucofitapp.data.mapper.toHistories
import com.fitcoders.glucofitapp.data.mapper.toHistory
import com.fitcoders.glucofitapp.utils.ResultWrapper
import com.fitcoders.glucofitapp.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScanHistoryRepositoryImpl(private val datasource: ScanHistoryApiDataSource) : ScanHistoryRepository {
    override fun getScanHistory(): Flow<ResultWrapper<List<History>>>{
        return flow {
            emit(ResultWrapper.Loading())
            delay(1000)
            val response = datasource.getAllHistoryData().data.toHistories()
            emit(ResultWrapper.Success(response))
        }
    }
}