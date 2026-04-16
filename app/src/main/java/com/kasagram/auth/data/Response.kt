package com.kasagram.auth.data

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)


@Serializable
data class RegisterRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val bio: String
)


@Serializable
data class AuthResponse(
    val token: String,
    val userId: Int,
    val username: String
)