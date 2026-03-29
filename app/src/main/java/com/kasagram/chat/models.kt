package com.kasagram.chat

import com.google.gson.annotations.SerializedName
import com.kasagram.auth.User


data class Chat (
    val id: Int,
    val participants: List<User>,
    val created: String
)


data class Message (
    val chat: Chat,
    val sender: User,
    val content: String,
    val created: String,

    @SerializedName("is_read")
    var isRead: Boolean, // VAR, бо коли юзер відкриє чат, ми зміним на true
    val parent: Message?  // VAL, бо батьківське повідомлення не міняється
)