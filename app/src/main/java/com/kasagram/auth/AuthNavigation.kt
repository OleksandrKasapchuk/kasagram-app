package com.kasagram.auth

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kasagram.auth.ui.ChangePasswordScreen
import com.kasagram.auth.ui.LoginScreen
import com.kasagram.auth.ui.ProfileScreen
import com.kasagram.auth.ui.RegisterScreen


fun NavGraphBuilder.authGraph(navController: NavController, onLogout: () -> Unit) {
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
                ProfileScreen(user = user, user.userPosts, navController, onLogout)
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
        RegisterScreen(
            navController,
            onLoginClick = { navController.navigate("login") }
        )
    }
    composable("change_password") { ChangePasswordScreen() }
}
