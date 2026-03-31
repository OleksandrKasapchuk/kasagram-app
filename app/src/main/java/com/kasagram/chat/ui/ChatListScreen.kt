package com.kasagram.chat.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kasagram.R
import com.kasagram.auth.data.AuthSession
import com.kasagram.chat.Chat
import com.kasagram.chat.ChatViewModel


@Composable
fun ChatListScreen(onChatClick: (Int) -> Unit, viewModel: ChatViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchChats()
    }

    Column (modifier = Modifier.fillMaxSize()) {
        if (viewModel.errorMessage != null) {
            Text(viewModel.errorMessage!!, color = Color.Red, modifier = Modifier.padding(16.dp))
        } else if (viewModel.isLoading && viewModel.chats.isEmpty()) {
            Text("Downloading chats...", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                itemsIndexed(viewModel.chats) { index, chat ->
                    ChatCard(chat, onChatClick)

                    // Якщо це останній елемент у списку — вантажимо наступну сторінку
                    if (index == viewModel.chats.lastIndex) {
                        LaunchedEffect(Unit) {
                            viewModel.fetchChats(isFirstPage = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatCard(chat: Chat, onChatClick: (Int) -> Unit) {
    val participant = chat.participants.find { it.id != AuthSession.userId }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onChatClick(chat.id) }
        ,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Шапка поста: Автор
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    // 1. ПЕРЕВІРКА: якщо media_url порожній, беремо локальну заглушку
                    model = participant?.avatarUrl ?: R.drawable.def_av,
                    contentDescription = "User avatar",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp),
                    contentScale = ContentScale.Crop, // Обрізає фото під розмір, щоб не було дірок
                    // 2. Поки фото вантажиться з інтернету, показуємо це:
                    placeholder = painterResource(R.drawable.loading_img),
                    // 3. Якщо сталася помилка завантаження:
                    error = painterResource(R.drawable.error_img)
                )

                Text(
                    text = participant?.username ?: "unluck",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}