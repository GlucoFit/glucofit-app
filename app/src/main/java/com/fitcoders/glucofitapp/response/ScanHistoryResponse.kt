package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class ScanHistoryResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: List<ScanHistoryData>
)

class ScanHistoryData {

}
