package com.fitcoders.glucofitapp.service.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


data class Data(
    @SerializedName("id")
    val id : Int,
    @SerializedName("objectImageUrl")
    val image : String,
    @SerializedName("objectName")
    val name : String,
    @SerializedName("objectSugar")
    val sugar: Int,
    @SerializedName("createdAt")
    val time: String
)
