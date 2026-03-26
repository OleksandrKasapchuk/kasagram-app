package com.kasagram.post

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasagram.auth.User // Твої моделі

@Composable
fun PostCard(post: Post) {
    // Card - це контейнер з тінню та закругленими кутами
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Шапка поста: Автор
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                // Тут пізніше буде аватарка, поки просто текст
                Text(
                    text = post.user.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Контент поста (Текст)
            Text(
                text = post.content ?: "",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Футер: Кнопки взаємодії
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (post.is_liked) "❤️ ${post.likes_count}" else "🤍 ${post.likes_count}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "💬 Коментувати",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Потужна штука для розробника: Прев'ю прямо в студії
@Preview(showBackground = true, name = "Post Card Preview")
@Composable
fun PostCardPreview() {
    // Створюємо фейкові дані, щоб не запускати сервер
    val mockUser = User(
        id = 1,
        username = "sasha_ukraine",
        avatar_url = null,
        bio = "Doing some Kotlin magic",
        is_online = true,
        first_name = "Sasha",
        last_name = "Dev"
    )

    val mockPost = Post(
        id = 101,
        user = mockUser,
        content = "Нарешті розібрався з імпортами в Котліні! Тепер дизайн виглядає як справжній додаток. 🚀",
        media_url = null,
        date_published = "2026-03-26",
        likes_count = 42,
        is_liked = true
    )
}