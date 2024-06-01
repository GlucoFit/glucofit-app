package com.fitcoders.glucofitapp.data

data class UserModel(
    val username: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)

