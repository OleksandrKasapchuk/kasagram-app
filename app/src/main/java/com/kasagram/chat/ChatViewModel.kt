package com.kasagram.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasagram.RetrofitClient
import com.kasagram.auth.User
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject


class ChatViewModel : ViewModel() {
    var chats by mutableStateOf<List<Chat>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private var nextPageUrl: String? = null
    private var currentPage = 1

    fun fetchChats(isFirstPage: Boolean = true) {
        if (isLoading) return
        if (!isFirstPage && nextPageUrl == null) return // Більше немає що вантажити

        viewModelScope.launch {
            isLoading = true
            try {
                val pageToLoad = if (isFirstPage) 1 else currentPage + 1
                val response = RetrofitClient.сhatApi.getChats(pageToLoad)

                chats = if (isFirstPage) {
                    response.results
                } else {
                    chats + response.results // Додаємо нові пости до старих
                }

                nextPageUrl = response.next
                currentPage = pageToLoad
            } catch (e: Exception) {
                errorMessage = "Could not download chats: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}


class ChatWebSocketManager(
    private val authToken: String,
    private val onMessageReceived: (String) -> Unit
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect(roomName: String) {
        val request = Request.Builder()
            .url("ws://10.0.2.2:8000/ws/chat/$roomName/") // 10.0.2.2 для емулятора
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

class MessageViewModel : ViewModel() {
    var messages by mutableStateOf<List<Message>>(emptyList())
    var participant by mutableStateOf<User?>(null)
    var isLoading by mutableStateOf(false)
    var isEndReached by mutableStateOf(false)
    var isPeerTyping by mutableStateOf(false) // Стан "друкує..."
    var errorMessage by mutableStateOf<String?>(null)
    var myUsername: String = ""
    private var wsManager: ChatWebSocketManager? = null

    // 1. Початкове завантаження повідомлень через API
    fun fetchMessages(chatId: Int, isFirstPage: Boolean = true) {
        if (isLoading || (isEndReached && !isFirstPage)) return
        viewModelScope.launch {
            isLoading = true
            try {
                val oldestId = if (isFirstPage) null else messages.lastOrNull()?.id
                val response = RetrofitClient.сhatApi.getMessages(chatId, oldestId)

                if (response.success) {
                    participant = response.participant
                    val newMessages = response.messages
                    if (newMessages.isEmpty()) {
                        isEndReached = true
                    } else {
                        messages = if (isFirstPage) newMessages else messages + newMessages
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Помилка завантаження: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // 2. Підключення до WebSocket для реального часу
    fun connectToChat(chatId: Int, token: String, currentUsername: String) {
        wsManager = ChatWebSocketManager(token) { jsonString ->
            // Ця лямбда виконується, коли приходить повідомлення з сервера
            handleIncomingWsEvent(jsonString, currentUsername)
        }
        wsManager?.connect(chatId.toString())
    }

    private fun handleIncomingWsEvent(jsonString: String, currentUsername: String) {
        val data = JSONObject(jsonString)
        val type = data.optString("type")

        when (type) {
            "user_typing" -> {
                // Відображаємо статус друку, якщо це не ми
                if (data.getString("username") != currentUsername) {
                    isPeerTyping = data.getBoolean("typing")
                }
            }
            "delete_message" -> {
                val id = data.getInt("message_id")
                messages = messages.filter { it.id != id }
            }
            "chat_message" -> { // ТЕПЕР ОБРОБЛЯЄМО ТІЛЬКИ ЦЕЙ ТИП
                val newMessage = parseJsonToMessage(data)
                messages = listOf(newMessage) + messages
            }
            else -> {
                println("Unknown type: $type")
            }
        }
    }

    // 3. Відправка повідомлення через сокет
    fun sendMessage(text: String, username: String, parentId: Int? = null) {
        wsManager?.sendMessage(text, username, parentId)
    }

    // 4. Очищення при закритті екрана
    override fun onCleared() {
        wsManager?.disconnect()
        super.onCleared()
    }

    private fun parseJsonToMessage(data: JSONObject): Message {
        // Створюємо об'єкт User для повідомлення
        val sender = User(
            id = 0, // ID можна не передавати через сокет, якщо воно не критичне для UI
            username = data.optString("username"),
            avatarUrl = null // Або додай у Django Consumer передачу аватара
        )

        return Message(
            id = data.optInt("message_id"),
            user = sender,
            content = data.optString("message"),
            timestamp = "", // Можна залишити пустим, бо ми використовуємо formattedTime
            formattedTime = data.optString("timestamp"), // У тебе в Django це '14:30'
            isRead = false,
            isMe = data.optString("username") == myUsername, // Django передає 'is_me'
            parentId = if (data.isNull("parent_id")) null else data.optInt("parent_id"),
            parentContent = data.optString("parent_content", null),
            parentUsername = data.optString("parent_username", null)
        )
    }
}