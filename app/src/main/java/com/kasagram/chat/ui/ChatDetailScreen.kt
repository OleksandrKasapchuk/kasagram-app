package com.kasagram.chat.ui

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasagram.auth.User
import com.kasagram.chat.Message
import com.kasagram.chat.viewmodel.MessageViewModel
import com.kasagram.post.ui.MessageInputField
import com.kasagram.post.ui.components.CustomImage
import kotlinx.coroutines.launch

@Composable
fun ChatDetailScreen(chatId: Int, onUserClick: (Int) -> Unit,onSendMessage: (String) -> Unit , viewModel: MessageViewModel) {
    val listState = rememberLazyListState()

    // Завантажуємо першу сторінку при вході
    LaunchedEffect(chatId) {
        viewModel.fetchMessages(chatId, isFirstPage = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 1. Хедер (закріплений зверху)
        Header(viewModel.participant, onUserClick)

        // 2. Список повідомлень (займає всю вільну вагу між хедером і полем введення)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f) // Це дозволяє списку скролитись і не вилазити на інші елементи
                .fillMaxWidth(),
            reverseLayout = true // Повідомлення будуть йти знизу вгору (як у Telegram)
        ) {
            // Якщо використовуєте reverseLayout, переконайтеся, що пагінація логічно вірна
            items(viewModel.messages) { message ->
                val scope = rememberCoroutineScope()
                MessageCard(message,
                    onReplyClick = { parentId ->
                        scope.launch {
                            val index = viewModel.messages.indexOfFirst { it.id == parentId }
                            if (index != -1) {
                                listState.animateScrollToItem(index)

                            }
                        }
                    })
            }

            item {
                if (viewModel.isLoading) {
                    Box(Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                } else if (!viewModel.isEndReached) {
                    LaunchedEffect(Unit) {
                        viewModel.fetchMessages(chatId, isFirstPage = false)
                    }
                }
            }
        }
        var chatMessage by remember { mutableStateOf("") }
        MessageInputField(
            value = chatMessage,
            onValueChange = { chatMessage = it },
            onSendClick = {
                if (chatMessage.isNotBlank()) {
                    onSendMessage(chatMessage)
                    chatMessage = "" // Очищаємо поле після відправки
                }
            },
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
            CustomImage(
                model = participant?.avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier.size(30.dp).clip(CircleShape),
                loadingSize = 15.dp
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = participant?.username ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    }
}

@Composable
fun MessageCard(message: Message, onReplyClick: (Int) -> Unit) {
    // 1. Визначаємо, чи це наше повідомлення
    val isMine = message.isMe

    // 2. Використовуємо Row, щоб розставити повідомлення по боках
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        // Якщо моє — притискаємо вправо (End), якщо чуже — вліво (Start)
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                // Обмежуємо ширину повідомлення, щоб воно не займало весь екран
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.75f)
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
                if (!message.parentContent.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black.copy(alpha = 0.05f))
                            .clickable { message.parentId?.let {onReplyClick(it) } }
                            .padding(start = 8.dp)
                    ) {
                        // Синя лінія збоку
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .align(Alignment.CenterVertically)
                                .background(MaterialTheme.colorScheme.primary)
                                .size(30.dp) // Висота підлаштується під контент
                        )
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = message.parentUsername ?: "",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = message.parentContent,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Text(
                    text = message.content,
                    color = if (isMine) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.formattedTime,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )

                    if (isMine) {
                        Spacer(Modifier.width(4.dp))
                        // Галочки: одна — відправлено, дві — прочитано
                        Text(
                            text = if (message.isRead) "✓✓" else "✓",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (message.isRead) Color(0xFF4CAF50) else Color.Gray
                        )
                    }
                }
            }
        }
    }
}