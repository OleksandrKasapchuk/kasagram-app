package com.kasagram.chat.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.kasagram.auth.User
import com.kasagram.chat.Message
import com.kasagram.chat.data.ChatWebSocketManager
import com.kasagram.core.bool
import com.kasagram.core.data.RetrofitClient
import com.kasagram.core.int
import com.kasagram.core.str
import com.kasagram.core.viewmodel.GlobalViewModel
import com.kasagram.core.viewmodel.SocketViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement


class MessageViewModel : SocketViewModel<ChatWebSocketManager>() {
    var messages by mutableStateOf<List<Message>>(emptyList())
    var participant by mutableStateOf<User?>(null)
    var isEndReached by mutableStateOf(false)
    var isPeerTyping by mutableStateOf(false)
    var myUsername: String = ""


    fun fetchMessages(chatId: Int, isFirstPage: Boolean = true) {
        if (isLoading || (isEndReached && !isFirstPage)) return

        launchWithLoading { // Використовуємо функцію з BaseViewModel
            val oldestId = if (isFirstPage) null else messages.lastOrNull()?.id
            val response = RetrofitClient.сhatApi.getMessages(chatId, oldestId)

            if (response.success) {
                participant = response.participant
                if (response.messages.isEmpty()) isEndReached = true
                messages = if (isFirstPage) response.messages else messages + response.messages
            }
        }
    }

    fun connectToChat(chatId: Int, token: String) {
        if (wsManager != null) {
            println("WS_LOG: Сокет вже підключений, ігноруємо дублікат")
            return
        }

        wsManager = ChatWebSocketManager(token) { json -> handleIncomingEvent(json) }
        wsManager?.connect("/ws/chat/$chatId/")

        // Відправляємо сигнал "прочитано" (можна з невеликою затримкою або через callback)
        wsManager?.markAsRead()
    }

    override fun onCleared() {
        wsManager?.disconnect()
        super.onCleared()
    }

    fun sendMessage(text: String, username: String, parentId: Int? = null) {
        wsManager?.sendChatMessage(text, username, parentId)
    }

    fun deleteMessage(messageId: Int){
        wsManager?.deleteMessage(messageId)
    }

    override fun handleIncomingEvent(jsonString: String) {
        val parser = RetrofitClient.json // Json конфіг
        val data = parser.parseToJsonElement(jsonString)

        when (data.str("type")) {
            "user_typing" -> isPeerTyping = data.bool("typing")
            "messages_read" -> {
                if (!data.bool("is_me"))
                    messages = messages.map { message ->
                        if (message.isMe && !message.isRead) {
                            message.copy(isRead = true)
                        } else {
                            message
                        }
                    }
            }
            "chat_message" -> {
                try {
                    val newMessage = parser.decodeFromJsonElement<Message>(data)
                    messages = listOf(newMessage) + messages
                } catch (e: Exception) {
                    println("WS_LOG: Error parsing message: ${e.message}")
                }
            }
            "delete_message" -> {
                val id = data.int("message_id")
                messages = messages.filter { it.id != id }
            }
            else -> {
                println("Unknown type: {type}")
            }
        }
    }

    fun observeGlobalChanges(globalViewModel: GlobalViewModel) {
        viewModelScope.launch {
            globalViewModel.userStatusEvent.collect { (username, isOnline) ->
                // Якщо цей юзер — той, з ким ми зараз спілкуємося
                if (participant?.username == username) {
                    participant = participant?.copy(isOnline = isOnline)
                }
            }
        }
    }
}