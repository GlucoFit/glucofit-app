package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("user")
    val user: User? = null,

    @field:SerializedName("token")
    val token: String? = null
)

data class User(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("userName")
    val userName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null
)
