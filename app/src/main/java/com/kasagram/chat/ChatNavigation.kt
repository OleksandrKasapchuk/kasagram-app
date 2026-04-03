package com.kasagram.chat

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kasagram.auth.data.AuthSession
import com.kasagram.chat.ui.ChatDetailScreen
import com.kasagram.chat.ui.ChatListScreen
import com.kasagram.chat.viewmodel.MessageViewModel


fun NavGraphBuilder.chatGraph(navController: NavController) {

    // Групуємо всі маршрути чату
    composable("chat_list") {
        ChatListScreen(
            onChatClick = { id ->
                navController.navigate("chat_detail/$id")
            }
        )
    }

    composable(
        route = "chat_detail/{chatId}",
        arguments = listOf(navArgument("chatId") { type = NavType.IntType })
    ) { backStackEntry ->
        val chatId = backStackEntry.arguments?.getInt("chatId") ?: 0
        val viewModel: MessageViewModel = viewModel()

        val token = AuthSession.token ?: ""
        val currentUsername = AuthSession.username ?: ""
        viewModel.myUsername = currentUsername // Обов'язково!

        LaunchedEffect(chatId) {
            viewModel.fetchMessages(chatId)
            viewModel.connectToChat(chatId, token, currentUsername)
        }

        ChatDetailScreen(chatId = chatId, onUserClick = { userId ->
            navController.navigate("profile/$userId")},
            onSendMessage = { text ->
                viewModel.sendMessage(text, currentUsername)
        }, viewModel)
    }
}