package com.kasagram.auth.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit


object AuthSession {
    private const val PREFS_NAME = "kasagram_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"

    private lateinit var prefs: android.content.SharedPreferences

    // Реактивні стейти для Compose
    var token by mutableStateOf<String?>(null)
        private set
    var userId by mutableIntStateOf(-1)
        private set

    fun init(context: android.content.Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
        token = prefs.getString(KEY_TOKEN, null)
        userId = prefs.getInt(KEY_USER_ID, -1)
    }

    fun updateSession(newToken: String?, newUserId: Int) {
        token = newToken
        userId = newUserId
        prefs.edit().apply {
            putString(KEY_TOKEN, newToken)
            putInt(KEY_USER_ID, newUserId)
            apply()
        }
    }

    val isLoggedIn: Boolean
        get() = !token.isNullOrEmpty()

    fun logout() {
        token = null
        userId = -1
        prefs.edit { clear() }
    }
}