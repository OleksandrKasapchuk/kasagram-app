package com.kasagram.notification


import com.kasagram.auth.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: Int,
    val actor: User,
    val message: String,
    @SerialName("target_url") val targetUrl: String,
    @SerialName("is_read") val isRead: Boolean,
    @SerialName("created_at_human") val timestamp: String // Використовуємо вже готову дату
)