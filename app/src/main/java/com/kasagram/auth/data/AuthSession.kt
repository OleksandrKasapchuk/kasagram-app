package com.kasagram.auth.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


object AuthSession {
    private const val PREFS_NAME = "kasagram_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"

    private lateinit var prefs: android.content.SharedPreferences

    // Реактивні стейти для Compose
    var token by mutableStateOf<String?>(null)
        private set
    var userId by mutableIntStateOf(-1)
        private set
    var username by mutableStateOf<String?>(null) // Реактивне поле для юзернейма
        private set

    fun init(context: android.content.Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
        token = prefs.getString(KEY_TOKEN, null)
        userId = prefs.getInt(KEY_USER_ID, -1)
        username = prefs.getString(KEY_USERNAME, null)
    }

    fun updateSession(newToken: String?, newUserId: Int, newUsername: String?) {
        token = newToken
        userId = newUserId
        username = newUsername

        prefs.edit().apply {
            putString(KEY_TOKEN, newToken)
            putInt(KEY_USER_ID, newUserId)
            putString(KEY_USERNAME, newUsername) // Зберігаємо в SharedPreferences
            apply()
        }
    }

    val isLoggedIn: Boolean
        get() = !token.isNullOrEmpty()

    fun logout() {
        token = null
        userId = -1
        username = null
        prefs.edit().clear().apply()
    }
}