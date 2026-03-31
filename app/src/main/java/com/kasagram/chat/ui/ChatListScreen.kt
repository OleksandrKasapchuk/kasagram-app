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
import androidx.compose.ui.text.font.FontWeight
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                CustomImage(
                    model =  chat.participant.avatarUrl,
                    contentDescription = "user avatar",
                    modifier = Modifier.clip(CircleShape)
                        .size(30.dp),
                    loadingSize = 15.dp
                )

                Text(
                    text = chat.participant.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}