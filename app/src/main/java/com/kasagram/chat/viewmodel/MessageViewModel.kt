package com.kasagram.chat.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.kasagram.auth.User
import com.kasagram.chat.Message
import com.kasagram.chat.data.ChatWebSocketManager
import com.kasagram.core.data.RetrofitClient
import com.kasagram.core.viewmodel.GlobalViewModel
import com.kasagram.core.viewmodel.SocketViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject


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
    }

    override fun onCleared() {
        wsManager?.disconnect()
        super.onCleared()
    }

    fun sendMessage(text: String, username: String, parentId: Int? = null) {
        wsManager?.sendChatMessage(text, username, parentId)
    }

    override fun handleIncomingEvent(jsonString: String) {
        val data = JSONObject(jsonString)
        when (data.optString("type")) {
            "user_typing" -> isPeerTyping = data.optBoolean("typing")
            "chat_message" -> {
                val newMessage = parseJsonToMessage(data)
                messages = listOf(newMessage) + messages
            }
            "delete_message" -> {
                val id = data.getInt("message_id")
                messages = messages.filter { it.id != id }
            }
            else -> {
                println("Unknown type: {type}")
            }
        }
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