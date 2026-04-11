package com.kasagram.chat.data

import com.kasagram.core.data.BaseWebSocketManager
import org.json.JSONObject


class ChatWebSocketManager(
    authToken: String,
    private val onMsg: (String) -> Unit
) : BaseWebSocketManager(authToken) {

    override fun onMessageReceived(text: String) = onMsg(text)
    override fun onConnectionFailed(t: Throwable) { /* обробка помилки */ }

    fun sendChatMessage(message: String, username: String, parentId: Int? = null) {
        val json = JSONObject().apply {
            put("action", "chat_message")
            put("message", message)
            put("username", username)
            parentId?.let { put("parent_id", it) }
        }
        sendMessage(json)
    }

    fun markAsRead() {
        val json = JSONObject().apply {
            put("action", "mark_as_read")
        }
        sendMessage(json)
    }

    fun deleteMessage(messageId: Int) {
        val json = JSONObject().apply {
            put("action", "delete")
            put("message_id", messageId)
        }
        sendMessage(json)
    }
}