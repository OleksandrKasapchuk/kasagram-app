package com.kasagram.post.ui.components

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kasagram.post.Post


@Composable
fun PostCard(post: Post, onUserClick: (Int) -> Unit, onLikeClick: (Int) -> Unit, navController: NavController) {
    var isLikedInternal by remember { mutableStateOf(post.isLiked) }
    var likesCountInternal by remember { mutableIntStateOf(post.likesCount) }

    // Якщо раптом пост прийшов оновлений ззовні (наприклад, після fetch), оновлюємо локальний стан
    LaunchedEffect(post.isLiked, post.likesCount) {
        isLikedInternal = post.isLiked
        likesCountInternal = post.likesCount
    }
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
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onUserClick(post.user.id) }
            ) {
                CustomImage(
                    model = post.user.avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(50.dp).clip(CircleShape),
                    loadingSize = 20.dp
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

            CustomImage(
                model = post.mediaUrl,
                contentDescription = "User post",
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
            Text(
                text = post.content ?: "",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            // Футер: Кнопки взаємодії
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        // 1. Міняємо стан локально (миттєвий відгук для юзера)
                        if (isLikedInternal) likesCountInternal-- else likesCountInternal++
                        isLikedInternal = !isLikedInternal

                        // 2. Викликаємо функцію, яку передали з Index (вона піде в ViewModel)
                        onLikeClick(post.id)
                    }
                ) {
                    // 4. ДИЗАЙН КНОПКИ ЗАЛЕЖИТЬ ВІД СТАНУ
                    Icon(
                        // Міняємо іконку (заповнена / контур)
                        imageVector = if (isLikedInternal) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        // Міняємо колір (AccentRed / сірий)
                        tint = if (isLikedInternal) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                        contentDescription = if (isLikedInternal) "Unlike" else "Like"
                    )
                }

                // КІЛЬКІСТЬ ЛАЙКІВ
                Text(
                    text = "$likesCountInternal likes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "💬 Comment",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.clickable{navController.navigate("post_detail/${post.id}")}
                )
            }
        }
    }
}