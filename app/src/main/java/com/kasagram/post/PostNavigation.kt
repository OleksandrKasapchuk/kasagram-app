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
        val postViewModel: PostViewModel = viewModel()
        val likeViewModel: LikeViewModel = viewModel()
        Index(
            viewModel = postViewModel,
            onUserClick = { userId -> navController.navigate("profile/$userId") },
            onLikeClick = { postId ->
                likeViewModel.likePost(postId) { response ->
                    postViewModel.updatePostLike(postId, response)
                }
            }, navController
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
        val postDetailViewModel: PostDetailViewModel = viewModel()
        val likeViewModel: LikeViewModel = viewModel()

        LaunchedEffect(postId) {
            if (postId != 0) {
                postDetailViewModel.loadPost(postId)
            }
        }

        PostDetailScreen(
            postDetailViewModel = postDetailViewModel,
            onLikeClick = { postId ->
                likeViewModel.likePost(postId) {    response ->
                    postDetailViewModel.updatePostLike(response)
                }
            },
            onDeletePost = { commentId -> println("Delete clicked for post $commentId") },
            onSendComment = { text, parentId ->
                println("Sending: $text (Parent: $parentId)")
            }
        )
    }
}
