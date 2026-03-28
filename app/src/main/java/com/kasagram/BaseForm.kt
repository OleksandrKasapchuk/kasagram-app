package com.kasagram

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

class GenericFormState(initialFields: List<String>) {
    // Карта, де ключ — назва поля (н-д "username"), а значення — MutableState
    val fields = initialFields.associateWith { mutableStateOf("") }
    val errors = initialFields.associateWith { mutableStateOf<String?>(null) }

    // Функція для отримання тексту з поля
    fun getValue(fieldName: String): String = fields[fieldName]?.value ?: ""

    // Функція для оновлення тексту
    fun onValueChange(fieldName: String, newValue: String) {
        fields[fieldName]?.value = newValue
    }

    fun setError(fieldName: String, message: String) {
        errors[fieldName]?.value = message
    }

    // Аналог form.is_valid()
    fun isValid(): Boolean {
        return fields.values.all { it.value.isNotBlank() }
    }
}

@Composable
fun FormField(
    label: String,
    fieldName: String,
    state: GenericFormState,
    isPassword: Boolean = false
) {
    val errorMessage = state.errors[fieldName]?.value
    OutlinedTextField(
        value = state.fields[fieldName]?.value ?: "",
        onValueChange = { state.onValueChange(fieldName, it) },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
    // Якщо є помилка — показуємо її знизу червоним текстом
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}