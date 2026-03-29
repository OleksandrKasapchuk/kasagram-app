package com.kasagram.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasagram.AuthSession
import com.kasagram.LoginRequest
import com.kasagram.RetrofitClient
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun login(request: LoginRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitClient.instance.login(request)

                // ЗАПИСУЄМО В СЕСІЮ
                AuthSession.token = response.token
                AuthSession.userId = response.user_id
                // Якщо у тебе є поле для об'єкта User:
                // AuthSession.currentUser = User(id = response.user_id, username = response.username ...)

                onSuccess() // Переходимо на головний екран
            } catch (e: Exception) {
                errorMessage = "Помилка: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}