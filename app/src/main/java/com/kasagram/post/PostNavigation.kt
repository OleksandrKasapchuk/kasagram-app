package com.kasagram.post

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kasagram.post.ui.AddPostScreen
import com.kasagram.post.ui.Index
import com.kasagram.post.ui.PostDetailScreen


fun NavGraphBuilder.postGraph(navController: NavController) {
    composable("index") {
        // Використовуємо функцію viewModel(), а не конструктор PostViewModel()
        // Це гарантує, що стан постів не зникатиме при навігації
        val viewModel: PostViewModel = viewModel()
        Index(
            viewModel = viewModel,
            onUserClick = { userId -> navController.navigate("profile/$userId") }
        )
    }

    composable("add_post") {
        // Отримуємо екземпляр нашої нової AndroidViewModel
        val createViewModel: CreatePostViewModel = viewModel()

        AddPostScreen(createViewModel.isUploading,
            onPostCreated = { data ->
                // Викликаємо метод завантаження
                createViewModel.uploadPost(data) {
                    // Цей блок {} виконається тільки після успішного API запиту (onComplete)
                    navController.navigate("index") {
                        // Очищуємо бекстек, щоб юзер не повернувся на екран створення кнопкою "Назад"
                        popUpTo("index") { inclusive = true }
                    }
                }
            }
        )
    }
    composable(
        route = "post_detail/{postId}",
        arguments = listOf(navArgument("postId") { type = NavType.IntType })
    ) { backStackEntry ->
        // 1. Отримуємо ID з параметрів шляху
        val postId = backStackEntry.arguments?.getInt("postId") ?: 0
        val viewModel: PostDetailViewModel = viewModel()

        LaunchedEffect(postId) {
            if (postId != 0) {
                viewModel.loadPost(postId)
            }
        }

        PostDetailScreen(
            viewModel = viewModel,
            onLikeClick = { println("Like clicked for post $postId") },
            onDeletePost = { commentId -> println("Delete clicked for post $commentId") },
            onSendComment = { text, parentId ->
                println("Sending: $text (Parent: $parentId)")
            }
        )
    }
}
