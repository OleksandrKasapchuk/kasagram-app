package com.kasagram.post

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun PostFeed(posts: List<Post>) {
    // LazyColumn - це як цикл for, але для UI зі скролом
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp) // Відступ знизу
    ) {
        items(posts) { post ->
            PostCard(post = post) // Викликаємо твою функцію для кожного поста
        }
    }
}