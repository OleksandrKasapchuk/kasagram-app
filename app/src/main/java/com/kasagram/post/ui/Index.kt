package com.kasagram.post.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kasagram.post.Post
import com.kasagram.post.PostViewModel
import com.kasagram.post.ui.components.PostCard

@Composable
fun Index(viewModel: PostViewModel = viewModel(), onUserClick: (Int) -> Unit) {
    // Завантажуємо пости при першому запуску
    LaunchedEffect(Unit) {
        viewModel.fetchPosts()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (viewModel.errorMessage != null) {
            Text(viewModel.errorMessage!!, color = Color.Red, modifier = Modifier.padding(16.dp))
        } else if (viewModel.isLoading && viewModel.posts.isEmpty()) {
            Text("Завантаження перших постів...", modifier = Modifier.padding(16.dp))
        } else {
            PostFeed(posts = viewModel.posts, viewModel = viewModel, onUserClick = onUserClick)
        }
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
fun PostFeed(posts: List<Post>, viewModel: PostViewModel, onUserClick: (Int) -> Unit){
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // 1. Спочатку малюємо Хедер
        item { Header() }

        itemsIndexed(posts) { index, post ->
            PostCard(post = post, onUserClick)

            // Якщо це останній елемент у списку — вантажимо наступну сторінку
            if (index == posts.lastIndex) {
                LaunchedEffect(Unit) {
                    viewModel.fetchPosts(isFirstPage = false)
                }
            }
        }
    }
}
