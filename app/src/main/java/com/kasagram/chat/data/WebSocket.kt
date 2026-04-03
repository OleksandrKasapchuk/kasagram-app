package com.kasagram.chat.data

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject


class ChatWebSocketManager(
    private val authToken: String,
    private val onMessageReceived: (String) -> Unit
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect(roomName: String) {
        val request = Request.Builder()
            .url("ws://10.0.2.2:8000/ws/chat/$roomName/")
            .addHeader("Authorization", "Token $authToken")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                onMessageReceived(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Connection failed: ${t.message}")
            }
        })
    }

    fun sendMessage(messageText: String, username: String, parentId: Int? = null) {
        val json = JSONObject().apply {
            put("action", "chat_message")
            put("message", messageText)
            put("username", username)
            parentId?.let { put("parent_id", it) }
        }
        webSocket?.send(json.toString())
    }

    fun disconnect() {
        webSocket?.close(1000, "Canceled by user")
        webSocket = null
    }
}