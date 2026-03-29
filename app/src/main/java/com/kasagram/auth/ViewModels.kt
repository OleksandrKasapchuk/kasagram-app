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

                onSuccess() // Переходимо на головний екран
            } catch (e: Exception) {
                errorMessage = "Помилка: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}

class ProfileViewModel : ViewModel() {
    var userState by mutableStateOf<User?>(null)
    var isLoading by mutableStateOf(false)

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                // Викликаємо Retrofit
                val user = RetrofitClient.instance.getUserDetail(userId)
                userState = user
            } catch (e: Exception) {
                println("Помилка: ${e.localizedMessage}")
            } finally {
                isLoading = false
            }
        }
    }
}