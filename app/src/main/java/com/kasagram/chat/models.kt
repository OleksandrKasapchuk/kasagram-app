package com.kasagram.chat


import com.kasagram.auth.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Chat (
    val id: Int,
    val participant: User,
    @SerialName("last_message") val lastMessage: Message?,
    @SerialName("unread_count") val unreadCount: Int
)


@Serializable
data class Message (
    val id: Int = -1,
    val user: User? = null,
    val content: String,
    val timestamp: String,
    @SerialName("formatted_time") val formattedTime: String,
    @SerialName("is_read") var isRead: Boolean = false,
    @SerialName("is_me") val isMe: Boolean,
    @SerialName("parent_id") val parentId: Int? = null,
    @SerialName("parent_content") val parentContent: String? = null,
    @SerialName("parent_username") val parentUsername: String? = null

)