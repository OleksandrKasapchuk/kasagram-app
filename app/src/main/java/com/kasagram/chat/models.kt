package com.kasagram.chat

import com.google.gson.annotations.SerializedName
import com.kasagram.auth.User


data class Chat (
    val id: Int,
    val participant: User,
    val created: String,
    @SerializedName("last_message") val lastMessage: Message?,
    @SerializedName("unread_count") val unreadCount: Int
)


data class Message (
    val id: Int,
    val user: User,
    val content: String,
    val timestamp: String,
    @SerializedName("formatted_time") val formattedTime: String,
    @SerializedName("is_read") var isRead: Boolean,
    @SerializedName("is_me") val isMe: Boolean,
    @SerializedName("parent_id") val parentId: Int?,
    @SerializedName("parent_content") val parentContent: String?,
    @SerializedName("parent_username") val parentUsername: String?
)