package com.kasagram.auth.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)


@Serializable
data class RegisterRequest(
    val username: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    val email: String,
    val password: String,
    val bio: String
)


@Serializable
data class AuthResponse(
    val token: String,
    @SerialName("user_id") val userId: Int,
    val username: String
)