package com.fitcoders.glucofitapp.data.mapper

import com.fitcoders.glucofitapp.data.History
import com.fitcoders.glucofitapp.service.network.model.Data
import com.fitcoders.glucofitapp.service.network.model.HistoryResponse

fun Data?.toHistory() =
    History(
        id = this?.id ?: 0,
        objectName = this?.name ?: "",
        objectImageUrl = this?.image ?: "",
        objectSugar = this?.sugar ?: 0,
        createdAt = this?.time ?: ""
    )

fun Collection<Data>?.toHistories() = this?.map { it.toHistory() } ?: listOf()