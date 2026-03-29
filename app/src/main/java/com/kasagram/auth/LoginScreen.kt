package com.kasagram.auth

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
import com.kasagram.LoginRequest


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

@Composable
fun RegistrationScreen(onLoginClick: () -> Unit) {
    // 1. Ініціалізуємо форму (як form = RegistrationForm())
    val form = remember {
        GenericFormState(listOf("username", "first_name", "last_name", "email", "password", "bio"))
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Register", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Просто перераховуємо поля
        FormField("Username", "username", form)
        FormField("first name", "first_name", form)
        FormField("last name", "last_name", form)
        FormField("Email", "email", form)
        FormField("Password", "password", form, isPassword = true)
        FormField("Bio", "bio", form)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Відправка на API */ },
            enabled = form.isValid(), // Кнопка активна тільки якщо все заповнено
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
        TextButton(onClick = onLoginClick) {
            Text("Already have an account? Log in", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun ChangePasswordScreen() {
    val form = remember {
        GenericFormState(listOf("password", "password1", "new_password2"))
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Change password", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        FormField("password", "password", form, isPassword = true)
        FormField("new password", "new_password1", form, isPassword = true)
        FormField("confirm the password", "new_password2", form, isPassword = true)

        Spacer(modifier = Modifier.height(24.dp))
        val passwordsMatch = form.getValue("password1") == form.getValue("password2")
        Button(
            onClick = { /* Відправка на API */ },
            enabled = form.isValid() && passwordsMatch,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}