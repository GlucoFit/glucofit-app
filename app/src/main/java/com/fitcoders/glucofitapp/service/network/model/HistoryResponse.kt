package com.fitcoders.glucofitapp.service.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName



@Keep
data class HistoryResponse(
    @SerializedName("message")
    val message : String,
    @SerializedName("data")
    val data : List<Data>?
)
