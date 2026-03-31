package com.kasagram.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kasagram.FormField
import com.kasagram.GenericFormState
import com.kasagram.auth.AuthViewModel
import com.kasagram.auth.data.LoginRequest


@Composable
fun LoginScreen(navController: NavController, onRegisterClick: () -> Unit, authViewModel: AuthViewModel = viewModel()) {
    val form = remember {
        GenericFormState(listOf("username", "password"))
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Login", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Просто перераховуємо поля
        FormField("Username", "username", form)
        FormField("Password", "password", form, isPassword = true)

        authViewModel.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val loginData = LoginRequest(
                    username = form.getValue("username"),
                    password = form.getValue("password")
                )
                // Викликаємо логіку з ViewModel
                authViewModel.login(loginData) {
                    // Цей блок виконається тільки при успіху (onSuccess)
                    navController.navigate("index") {
                        popUpTo("login") { inclusive = true } // Видаляємо екран логіну з бекстеку
                    }
                }
            },
            enabled = form.isValid() && !authViewModel.isLoading, // Блокуємо кнопку при завантаженні
            modifier = Modifier.fillMaxWidth()
        ) {
            if (authViewModel.isLoading) {
                Text("Loading...") // Тут можна поставити CircularProgressIndicator
            } else {
                Text("Login")
            }
        }

        TextButton(onClick = onRegisterClick) {
            Text("Don't have an account? Register here", color = MaterialTheme.colorScheme.primary)
        }
    }
}