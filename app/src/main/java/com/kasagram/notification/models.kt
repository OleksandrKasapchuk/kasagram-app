package com.kasagram.notification


import com.kasagram.auth.User
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: Int,
    val actor: User,
    val message: String,
    val targetUrl: String,
    val isRead: Boolean,
    val timestamp: String // Використовуємо вже готову дату
)