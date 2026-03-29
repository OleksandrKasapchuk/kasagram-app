package com.kasagram.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kasagram.R


@Composable
fun PostCard(post: Post, onUserClick: (Int) -> Unit) {
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var likesCount by remember { mutableStateOf(post.likesCount) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.clickable { onUserClick(post.user.id) }
            ) {
                AsyncImage(
                    // 1. ПЕРЕВІРКА: якщо media_url порожній, беремо локальну заглушку
                    model = post.user.avatarUrl ?: R.drawable.def_av,
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
                    text = post.user.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(16.dp))
            // Card - це контейнер з тінню та закругленими кутами
            AsyncImage(
                // 1. ПЕРЕВІРКА: якщо media_url порожній, беремо локальну заглушку
                model = post.mediaUrl,
                contentDescription = "Post Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop, // Обрізає фото під розмір, щоб не було дірок
                // 2. Поки фото вантажиться з інтернету, показуємо це:
                placeholder = painterResource(R.drawable.loading_img),
                // 3. Якщо сталася помилка завантаження:
                error = painterResource(R.drawable.error_img)
            )
            // Контент поста (Текст)
            Text(
                text = post.content ?: "",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            // Футер: Кнопки взаємодії
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // НАША ІНТЕРАКТИВНА КНОПКА ЛАЙКА
                IconButton(
                    onClick = {
                        // 3. ОНОВЛЮЄМО СТАН ПРИ КЛІКУ
                        if (isLiked) {
                            likesCount-- // Якщо був лайк - прибираємо
                        } else {
                            likesCount++ // Якщо не було - додаємо
                        }
                        isLiked = !isLiked // Перемикаємо стан
                    }
                ) {
                    // 4. ДИЗАЙН КНОПКИ ЗАЛЕЖИТЬ ВІД СТАНУ
                    Icon(
                        // Міняємо іконку (заповнена / контур)
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        // Міняємо колір (AccentRed / сірий)
                        tint = if (isLiked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                        contentDescription = if (isLiked) "Unlike" else "Like"
                    )
                }

                // КІЛЬКІСТЬ ЛАЙКІВ
                Text(
                    text = "$likesCount likes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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