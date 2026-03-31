package com.kasagram.notification

import com.kasagram.auth.User

data class Notification(
    val id: Int,
    val actor: User,
    val message: String,
    val targetUrl: String, // Аналог notification.get_url
    val timestamp: String,
    val isRead: Boolean = false
)