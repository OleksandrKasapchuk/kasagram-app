package com.kasagram.chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasagram.RetrofitClient
import com.kasagram.chat.Chat
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