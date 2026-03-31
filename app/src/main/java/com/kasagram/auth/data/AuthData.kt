package com.kasagram.auth.data

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    val username: String,
    val password: String
)


data class RegisterRequest(
    val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String,
    val password: String,
    val bio: String
)


data class AuthResponse(
    val token: String,
    @SerializedName("user_id") val userId: Int,
    val username: String
)