package com.kasagram.notification

import com.google.gson.annotations.SerializedName
import com.kasagram.auth.User


data class Notification(
    val id: Int,
    val actor: User,
    val message: String,
    @SerializedName("target_url") val targetUrl: String,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("created_at_human") val timestamp: String // Використовуємо вже готову дату
)