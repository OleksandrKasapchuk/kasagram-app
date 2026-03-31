package com.kasagram.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kasagram.FormField
import com.kasagram.GenericFormState


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