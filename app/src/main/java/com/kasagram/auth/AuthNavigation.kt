package com.kasagram.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kasagram.AuthSession
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
        ProfileScreen(user = userToShow, userPosts = userPosts, navController)
    }
    composable("login") {
        LoginScreen(
            onRegisterClick = { navController.navigate("register") }
        )
    }

    composable("register") {
        RegistrationScreen(
            onLoginClick = { navController.navigate("login") }
        )
    }
    composable("change_password") { ChangePasswordScreen() }
    composable("logout") { logout(navController) }
}

fun logout(navController: NavController) {
    // 1. Очищаємо сесію
    AuthSession.token = ""
    AuthSession.currentUser = null

    // 2. Викидаємо юзера на екран логіну
    navController.navigate("login") {
        // Очищаємо всю історію переходів, щоб не можна було натиснути "назад" і повернутися в профіль
        popUpTo(0) { inclusive = true }
    }
}