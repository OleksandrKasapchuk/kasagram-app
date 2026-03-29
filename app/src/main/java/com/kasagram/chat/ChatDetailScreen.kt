package com.kasagram.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kasagram.AuthSession
import com.kasagram.R
import com.kasagram.auth.User
import com.kasagram.messagesList
import com.kasagram.post.MessageInputField

@Composable
fun ChatDetailScreen(chat: Chat, onUserClick: (Int) -> Unit) {
    val participant = chat.participants.find { it.id != AuthSession.userId }
    var chatMessage by remember { mutableStateOf("") }
    Column (modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Header(participant, onUserClick)
            }
            val chatMessages = messagesList.filter { it.chat.id == chat.id }

            items(chatMessages) { message ->
                MessageCard(message)
            }
        }
        MessageInputField(
            value = chatMessage,
            onValueChange = { chatMessage = it },
            onSendClick = { /* API Send Message to Chat */ },
            placeholder = "Message..."
        )
    }
}

@Composable
fun Header(participant: User?, onUserClick: (Int) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable { participant?.let { onUserClick(it.id) } }
        ) {
            AsyncImage(
                model = participant?.avatarUrl ?: R.drawable.def_av,
                contentDescription = "User avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(30.dp),
                contentScale = ContentScale.Crop,

                placeholder = painterResource(R.drawable.loading_img),
                // 3. Якщо сталася помилка завантаження:
                error = painterResource(R.drawable.error_img)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = participant?.username ?: "grr",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    }
}

@Composable
fun MessageCard(message: Message) {
    // 1. Визначаємо, чи це наше повідомлення
    val isMine = message.sender.id == AuthSession.userId

    // 2. Використовуємо Row, щоб розставити повідомлення по боках
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        // Якщо моє — притискаємо вправо (End), якщо чуже — вліво (Start)
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                // Обмежуємо ширину повідомлення, щоб воно не займало весь екран
                .fillMaxWidth(0.6f)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMine) 16.dp else 0.dp, // Хвостик зліва для чужого
                        bottomEnd = if (isMine) 0.dp else 16.dp    // Хвостик справа для твого
                    )
                )
                // Колір: синюватий для тебе, сірий для іншого
                .background(
                    if (isMine) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.outline
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = message.content,
                    color = if (isMine) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
                Text(
                    text = "12:00", // Сюди потім підставиш реальний час
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}