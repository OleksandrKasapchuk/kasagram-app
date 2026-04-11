package com.kasagram.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kasagram.R
import com.kasagram.post.ui.components.CustomImage


@Composable
fun NotificationListScreen(
    viewModel: NotificationViewModel = viewModel(), // Передаємо в'юмодель
    onUserClick: (Int) -> Unit,
    onNotificationClick: (String) -> Unit
) {
    // Викликаємо завантаження один раз при вході на екран
    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

    val notifications = viewModel.notifications

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Notifications",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (notifications.isEmpty()) {
            // Можна додати Empty State
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications yet")
            }
        } else {
            LazyColumn {
                items(notifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        onUserClick = onUserClick,
                        onNotificationClick = onNotificationClick
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }
}
@Composable
fun NotificationItem(
    notification: Notification,
    onUserClick: (Int) -> Unit,
    onNotificationClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                // Якщо не прочитано — підсвічуємо ледь помітним кольором
                if (!notification.isRead) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                else Color.Transparent
            )
            .clickable { onNotificationClick(notification.targetUrl) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomImage(
            model =  notification.actor.avatarUrl?: R.drawable.def_av,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .clickable { onUserClick(notification.actor.id) },
            loadingSize = 25.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Текст сповіщення
        Column(modifier = Modifier.weight(1f)) {
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(notification.actor.username)
                }
                append(" ${notification.message}")
            }, fontSize = 14.sp)

            Text(
                text = notification.timestamp,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Маленька крапка, якщо не прочитано
        if (!notification.isRead) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}