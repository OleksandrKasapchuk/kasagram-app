package com.kasagram.core.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    // Спільні стани для всіх екранів
    var isLoading by mutableStateOf(false)
        protected set // Змінювати може тільки ViewModel

    var errorMessage by mutableStateOf<String?>(null)
        protected set

    // Універсальна функція для запуску запитів з обробкою помилок
    protected fun launchWithLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                block()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Unknown Error"
            } finally {
                isLoading = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Тут можна додати загальну очистку ресурсів
    }
}