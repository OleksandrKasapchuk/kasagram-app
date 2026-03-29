package com.kasagram.post

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


fun NavGraphBuilder.postGraph(navController: NavController) {
    composable("index") {
        Index(
            viewModel = PostViewModel(),
            onUserClick = { userId -> navController.navigate("profile/$userId")})
    }

//    composable("add_post") {
//        AddPostScreen(
//            onPostCreated = { data ->
//                // 1. Дістаємо дані з форми
//                val caption = data["caption"] ?: ""
//                val imageUri = data["image"] ?: "" // Це Uri з галереї!
//
//                // 2. Створюємо новий об'єкт Post
//                val newPost = Post(
//                    // Генеруємо тимчасовий ID (наприклад, timestamp)
//                    id = System.currentTimeMillis().toInt(),
//                    // Автором ставимо поточного залогіненого юзера
//                    user = AuthSession.currentUser ?: author,
//                    // ВАЖЛИВО: AsyncImage в Index спокійно прочитає цей imageUri
//                    mediaUrl = imageUri,
//                    content = caption,
//                    datePublished = "now",
//                    isLiked = false,
//                    likesCount = 0
//                )
//
//
//                // 4. Повертаємося на головну
//                navController.navigate("index") {
//                    popUpTo("index") { inclusive = true }
//                }
//            }
//        )
//    }
//    composable(
//        route = "post_detail/{postId}",
//        arguments = listOf(navArgument("postId") { type = NavType.IntType })
//    ) { backStackEntry ->
//        // 1. Отримуємо ID з параметрів шляху
//        val postId = backStackEntry.arguments?.getInt("postId") ?: 0
//
//        // 2. Шукаємо пост у нашому списку (як Post.objects.get(pk=postId))
//        val post = mockPosts.find { it.id == postId }
//
//        if (post != null) {
//            PostDetailScreen(
//                post = post,
//                comments = emptyList(), // Поки що пустий список або твої mockComments
//                onLikeClick = {
//                    // Логіка лайка (імітація)
//                    println("Liked post ${post.id}")
//                },
//                onDeletePost = {
//                    mockPosts.remove(post)
//                    navController.popBackStack()
//                },
//                onSendComment = { text, parentId ->
//                    println("Sending comment: $text to parent: $parentId")
//                    // Тут буде створення об'єкта Comment і додавання в список
//                }
//            )
//        }
//    }
}
