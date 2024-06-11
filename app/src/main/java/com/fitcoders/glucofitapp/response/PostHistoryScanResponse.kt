package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName
data class PostHistoryScanResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<UploadScanResult?>? = null
)

data class UploadScanResult(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("objectImageUrl")
    val objectImageUrl: String? = null,

    @field:SerializedName("objectName")
    val objectName: String? = null,

    @field:SerializedName("objectSugar")
    val objectSugar: Int? = null,

    @field:SerializedName("userId")
    val userId: Int? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null

)