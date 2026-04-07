package com.kasagram.chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasagram.chat.Chat
import com.kasagram.core.data.RetrofitClient
import com.kasagram.core.viewmodel.GlobalViewModel
import kotlinx.coroutines.launch

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

    // У ChatViewModel
    fun observeGlobalStatus(globalViewModel: GlobalViewModel) {
        viewModelScope.launch {
            globalViewModel.userStatusEvent.collect { (username, isOnline) ->
                // Оновлюємо статус у списку чатів ChatViewModel
                chats = chats.map { chat ->
                    if (chat.participant.username == username) {
                        chat.copy(participant = chat.participant.copy(isOnline = isOnline))
                    } else {
                        chat
                    }
                }
            }
        }
    }
}