package com.kasagram.core.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.kasagram.chat.Chat
import com.kasagram.core.data.GlobalWebSocketManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject


class GlobalViewModel: SocketViewModel<GlobalWebSocketManager>() {
    var chats by mutableStateOf<List<Chat>>(emptyList())
    private val _userStatusEvent = MutableSharedFlow<Pair<String, Boolean>>(extraBufferCapacity = 1)
    val userStatusEvent = _userStatusEvent.asSharedFlow()

    override fun handleIncomingEvent(jsonString: String) {
        val data = JSONObject(jsonString)
        when (data.optString("type")) {
            "user_status_change" -> {
                val username = data.getString("username")
                val isOnline = data.getBoolean("is_online")

                // Оновлюємо статус юзера в списку чатів
                chats = chats.map { chat ->
                    if (chat.participant.username == username) {
                        chat.copy(participant = chat.participant.copy(isOnline = isOnline))
                    } else {
                        chat
                    }
                }
                viewModelScope.launch {
                    _userStatusEvent.emit(username to isOnline)
                }
            }
        }
    }

    fun connect(token: String) {
        if (wsManager != null) {
            println("WS_LOG: Сокет вже підключений, ігноруємо дублікат")
            return
        }
        wsManager = GlobalWebSocketManager(token) { json -> handleIncomingEvent(json) }
        wsManager?.connect("/ws/global/")
    }
}