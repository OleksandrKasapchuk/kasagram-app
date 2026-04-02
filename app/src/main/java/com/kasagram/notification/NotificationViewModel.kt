package com.kasagram.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasagram.RetrofitClient
import kotlinx.coroutines.launch


class NotificationViewModel() : ViewModel() {
    var notifications by mutableStateOf<List<Notification>>(emptyList())
        private set

    fun loadNotifications() {
        viewModelScope.launch {
            try {
                notifications = RetrofitClient.notificationApi.getNotifications()
            } catch (e: Exception) {
                // обробка помилок
            }
        }
    }
}