package com.kasagram.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kasagram.chat.Chat
import com.kasagram.chat.ChatViewModel
import com.kasagram.post.ui.components.CustomImage

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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onChatClick(chat.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар
            CustomImage(
                model = chat.participant.avatarUrl,
                contentDescription = "user avatar",
                modifier = Modifier.size(50.dp).clip(CircleShape),
                loadingSize = 20.dp
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Верхній рядок: Ім'я + (Галочка + Час)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.participant.username ?: "Unknown",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    // Секція статусу та часу (як на фото 2)
                    chat.lastMessage?.let { msg ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Галочки тільки якщо повідомлення наше
                            if (msg.isUserMessage) {
                                val icon = if (msg.isRead) Icons.Default.DoneAll else Icons.Default.Check
                                val color = if (msg.isRead) Color(0xFF4FC3F7) else Color.White // Блакитний якщо прочитано

                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp).padding(end = 4.dp),
                                    tint = color
                                )
                            }

                            Text(
                                text = msg.formattedTime ?: "",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))

                // Нижній рядок: Текст повідомлення + Індикатор непрочитаних
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val previewText = if (chat.lastMessage != null) {
                        val prefix = if (chat.lastMessage.isUserMessage) "You: " else ""
                        "$prefix${chat.lastMessage.content ?: ""}"
                    } else {
                        "No messages yet"
                    }

                    Text(
                        text = previewText,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (chat.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(20.dp)
                                .background(MaterialTheme.colorScheme.tertiary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = chat.unreadCount.toString(),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}