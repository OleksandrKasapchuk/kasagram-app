package com.kasagram.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Index(posts: List<Post>, onUserClick: (Int) -> Unit) {
    if (posts.isEmpty()) {
        // Якщо постів немає, показуємо заглушку
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Постів поки немає. Підпишіться на когось!")
        }
    } else {
        PostFeed(posts, onUserClick)
    }
}

@Composable
fun Header() {
    Column {
        Row(
            modifier = Modifier
                .padding(16.dp) // Додай відступи, щоб текст не лип до країв
                .fillMaxWidth()
        ) {
            Text(
                text = "For you",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp)) // Відступ між словами
            Text(
                text = "Following",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // Лінія-розділювач (опціонально, як в інсті)
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    }
}

@Composable
fun PostFeed(posts: List<Post>, onUserClick: (Int) -> Unit){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // 1. Спочатку малюємо Хедер
        item {
            Header()
        }

        // 2. Потім малюємо всі пости
        items(posts) { post ->
            PostCard(post = post, onUserClick)
        }
    }
}