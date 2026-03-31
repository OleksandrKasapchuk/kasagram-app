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
import com.kasagram.auth.data.RegisterRequest


@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = viewModel(), onLoginClick: () -> Unit) {
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
            onClick = {
                val registerData = RegisterRequest(
                    username = form.getValue("username"),
                    password = form.getValue("password"),
                    firstName = form.getValue("first_name"),
                    lastName = form.getValue("last_name"),
                    email = form.getValue("email"),
                    bio = form.getValue("bio")
                )
                // Викликаємо логіку з ViewModel
                authViewModel.register(registerData) {
                    // Цей блок виконається тільки при успіху (onSuccess)
                    navController.navigate("index") {
                        popUpTo("register") { inclusive = true } // Видаляємо екран логіну з бекстеку
                    }
                }
            },
            enabled = form.isValid() && !authViewModel.isLoading, // Блокуємо кнопку при завантаженні
            modifier = Modifier.fillMaxWidth()
        ) {
            if (authViewModel.isLoading) {
                Text("Loading...") // Тут можна поставити CircularProgressIndicator
            } else {
                Text("Register")
            }
        }
        TextButton(onClick = onLoginClick) {
            Text("Already have an account? Log in", color = MaterialTheme.colorScheme.primary)
        }
    }
}
