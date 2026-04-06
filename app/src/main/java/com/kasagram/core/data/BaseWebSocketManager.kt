package com.kasagram.core.data

import android.util.Log
import com.kasagram.core.Config
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

abstract class BaseWebSocketManager(private val authToken: String) {
    protected val client = OkHttpClient()
    protected var webSocket: WebSocket? = null

    // Абстрактні методи, які кожен сокет реалізує по-своєму
    abstract fun onMessageReceived(text: String)
    abstract fun onConnectionFailed(t: Throwable)

    fun connect(urlPath: String) {
        val request = Request.Builder()
            .url("ws://${Config.HOST_NAME}$urlPath")
            .addHeader("Authorization", "Token $authToken")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                onMessageReceived(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WS_BASE", "Error: ${t.message}")
                onConnectionFailed(t)
            }
        })
    }

    fun sendMessage(json: JSONObject) {
        webSocket?.send(json.toString())
    }

    fun disconnect() {
        webSocket?.close(1000, "Canceled by user")
        webSocket = null
    }
}