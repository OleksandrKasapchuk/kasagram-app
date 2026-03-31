package com.kasagram.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasagram.RetrofitClient
import com.kasagram.auth.User
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
}


class MessageViewModel : ViewModel() {
    var messages by mutableStateOf<List<Message>>(emptyList())
    var participant by mutableStateOf<User?>(null)
    var isLoading by mutableStateOf(false)
    var isEndReached by mutableStateOf(false) // Чи завантажили ми все
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchMessages(chatId: Int, isFirstPage: Boolean = true) {
        if (isLoading || (isEndReached && !isFirstPage)) return

        viewModelScope.launch {
            isLoading = true
            try {
                // Визначаємо oldestId: якщо вантажимо наступну сторінку, беремо ID останнього повідомлення
                val oldestId = if (isFirstPage) null else messages.lastOrNull()?.id

                val response = RetrofitClient.сhatApi.getMessages(chatId, oldestId)

                if (response.success) {
                    val newMessages = response.messages
                    participant = response.participant
                    if (newMessages.isEmpty()) {
                        isEndReached = true
                    } else {
                        messages = if (isFirstPage) {
                            newMessages
                        } else {
                            // Додаємо в кінець списку (бо бекенд повертає старіші за ID)
                            messages + newMessages
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Помилка завантаження: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}