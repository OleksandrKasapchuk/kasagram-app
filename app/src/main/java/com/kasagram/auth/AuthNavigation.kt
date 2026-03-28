package com.kasagram.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kasagram.author
import com.kasagram.author2
import com.kasagram.author3
import com.kasagram.mockPosts


fun NavGraphBuilder.authGraph(navController: NavController) {
    composable(
        route = "profile/{userId}",
        arguments = listOf(navArgument("userId") { type = NavType.IntType })
    ) { backStackEntry ->
        // 1. Дістаємо ID з аргументів маршруту
        val userId = backStackEntry.arguments?.getInt("userId") ?: author.id

        // Тимчасовий список усіх юзерів для пошуку (поки немає бази даних)
        val allUsers = listOf(author, author2, author3)

        // Тепер шукаємо серед УСІХ юзерів, а не тільки тих, хто зробив пост
        val userToShow = allUsers.find { it.id == userId } ?: author
        // 3. Фільтруємо пости саме для цього юзера
        val userPosts = mockPosts.filter { it.user.id == userId }

        // 4. Віддаємо дані в екран
        ProfileScreen(user = userToShow, userPosts = userPosts)
    }
}