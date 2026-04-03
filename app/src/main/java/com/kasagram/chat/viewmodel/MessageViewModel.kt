package com.kasagram.chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasagram.RetrofitClient
import com.kasagram.auth.User
import com.kasagram.chat.Message
import com.kasagram.chat.data.ChatWebSocketManager
import kotlinx.coroutines.launch
import org.json.JSONObject



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