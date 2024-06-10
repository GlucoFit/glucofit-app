package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("message") val message: String
)