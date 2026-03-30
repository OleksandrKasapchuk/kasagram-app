package com.kasagram.auth

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kasagram.AuthSession


fun NavGraphBuilder.authGraph(navController: NavController) {
    composable(
        route = "profile/{userId}",
        arguments = listOf(navArgument("userId") { type = NavType.IntType })
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt("userId") ?: -1
        val viewModel: ProfileViewModel = viewModel()

        // Викликаємо завантаження при вході на екран
        LaunchedEffect(userId) {
            if (userId != -1) {
                viewModel.loadUser(userId)
            }
        }

        if (viewModel.isLoading) {
            // Покажи індикатор завантаження
            CircularProgressIndicator()
        } else {
            viewModel.userState?.let { user ->
                ProfileScreen(user = user, user.userPosts, navController)
            }
        }
    }
    composable("login") {
        LoginScreen(
            navController, 
            onRegisterClick = { navController.navigate("register") }
        )
    }

    composable("register") {
        RegistrationScreen(
            onLoginClick = { navController.navigate("login") }
        )
    }
    composable("change_password") { ChangePasswordScreen() }
    
    composable("logout") {
        LaunchedEffect(Unit) {
            AuthSession.logout()
            navController.navigate("login") {
                // Очищаємо всю історію переходів, щоб не можна було натиснути "назад"
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
