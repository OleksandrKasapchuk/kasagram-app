package com.kasagram.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kasagram.R
import com.kasagram.auth.User
import com.kasagram.post.Post


@Composable
fun ProfileScreen(user: User, userPosts: List<Post>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // 1. Додаємо хедер як один елемент, що займає всі 3 колонки (span)
        item(span = { GridItemSpan(3) }) {
            Column {
                ProfileHeader(user)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // 2. Логіка відображення постів або тексту
        if (userPosts.isEmpty()) {
            item(span = { GridItemSpan(3) }) {
                Text(
                    text = "У цього користувача ще немає постів",
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        } else {
            items(userPosts) { post ->
                PostThumbnail(post, navController)
            }
        }
    }
}

@Composable
fun PostThumbnail(post: Post, navController: NavController) {
    AsyncImage(
        model = post.mediaUrl,
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .clickable{navController.navigate("post_detail/${post.id}")},
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ProfileHeader(user: User){
    // 1. ШАПКА (Аватар + Стати)
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = user.avatarUrl ?: R.drawable.def_av,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape), // Modifier.clip робить аватарку круглою!
            contentScale = ContentScale.Crop
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 2. БІО
    Text(text = "${user.firstName} ${user.lastName}", fontWeight = FontWeight.Bold)
    Text(text = user.bio ?: "")
}