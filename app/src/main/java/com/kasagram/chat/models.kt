package com.kasagram.chat

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
    var is_read: Boolean, // VAR, бо коли юзер відкриє чат, ми зміним на true
    val parent: Message?  // VAL, бо батьківське повідомлення не міняється
)