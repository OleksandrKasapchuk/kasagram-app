package com.kasagram.post

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kasagram.AuthSession
import com.kasagram.author
import com.kasagram.mockPosts


fun NavGraphBuilder.postGraph(navController: NavController) {
    composable("index") {
        Index(
            posts = mockPosts,
            onUserClick = { userId -> navController.navigate("profile/$userId")})
    }

    composable("add_post") {
        AddPostScreen(
            onPostCreated = { data ->
                // 1. Дістаємо дані з форми
                val caption = data["caption"] ?: ""
                val imageUri = data["image"] ?: "" // Це Uri з галереї!

                // 2. Створюємо новий об'єкт Post
                val newPost = Post(
                    // Генеруємо тимчасовий ID (наприклад, timestamp)
                    id = System.currentTimeMillis().toInt(),
                    // Автором ставимо поточного залогіненого юзера
                    user = AuthSession.currentUser ?: author,
                    // ВАЖЛИВО: AsyncImage в Index спокійно прочитає цей imageUri
                    media_url = imageUri,
                    content = caption,
                    date_published = "now",
                    is_liked = false,
                    likes_count = 0
                )

                // 3. ДОДАЄМО В ПОЧАТОК СПИСКУ (як в Instagram)
                mockPosts.add(0, newPost)

                // 4. Повертаємося на головну
                navController.navigate("index") {
                    popUpTo("index") { inclusive = true }
                }
            }
        )
    }
}
