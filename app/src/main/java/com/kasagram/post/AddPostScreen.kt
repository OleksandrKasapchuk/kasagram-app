package com.kasagram.post

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kasagram.FormField
import com.kasagram.GenericFormState


@Composable
fun AddPostScreen(onPostCreated: (Map<String, String>) -> Unit) {
    // 1. Використовуємо наш універсальний стейт
    val form = remember {
        GenericFormState(listOf("caption", "image_uri"))
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("New Post", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Кастомний елемент для картинки, який працює з формою
        ImagePickerField(
            fieldName = "image_uri",
            state = form
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Наш універсальний FormField для тексту
        FormField(
            label = "Write a caption...",
            fieldName = "caption",
            state = form
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val data = mapOf(
                    "caption" to form.getValue("caption"),
                    "image" to form.getValue("image_uri")
                )
                onPostCreated(data)
            },
            // Кнопка активна, тільки якщо форма валідна (поля не порожні)
            enabled = form.isValid(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Share Post")
        }
    }
}


@Composable
fun ImagePickerField(fieldName: String, state: GenericFormState) {
    // 1. Використовуємо спеціальний контракт для медіа
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        // Записуємо шлях до вибраного фото
        uri?.let { state.onValueChange(fieldName, it.toString()) }
    }

    val currentUri = state.fields[fieldName]?.value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                // 2. Запускаємо саме фото-пікер (тільки зображення)
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
        contentAlignment = Alignment.Center
    ) {
        if (currentUri.isNullOrBlank()) {
            Icon(Icons.Default.AddAPhoto, modifier = Modifier.size(48.dp), contentDescription = null)
        } else {
            AsyncImage(
                model = currentUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}