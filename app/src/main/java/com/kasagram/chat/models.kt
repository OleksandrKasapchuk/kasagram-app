package com.kasagram.chat


import com.kasagram.auth.User
import kotlinx.serialization.Serializable


@Serializable
data class Chat (
    val id: Int,
    val participant: User,
    val lastMessage: Message?,
    val unreadCount: Int
)


@Serializable
data class Message (
    val id: Int = -1,
    val user: User? = null,
    val content: String,
    val timestamp: String,
    val formattedTime: String = "",
    var isRead: Boolean = false,
    val isMe: Boolean,
    val parentId: Int? = null,
    val parentContent: String? = null,
    val parentUsername: String? = null
)