package com.kasagram.post

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kasagram.post.ui.AddPostScreen
import com.kasagram.post.ui.Index


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
