package com.kasagram.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kasagram.chatList


fun NavGraphBuilder.chatGraph(navController: NavController) {
    // Групуємо всі маршрути чату
    composable("chat_list") {
        ChatListScreen(
            onChatClick = { chatId ->
                navController.navigate("chat_detail/$chatId")
            },
            chatList = chatList
        )
    }

    composable(
        route = "chat_detail/{chatId}",
        arguments = listOf(navArgument("chatId") { type = NavType.IntType })
    ) { backStackEntry ->
        val chatId = backStackEntry.arguments?.getInt("chatId") ?: 0
        val chat = chatList.find { it.id == chatId } // Беремо з твоїх моків

        if (chat != null) {
            ChatDetailScreen(chat = chat, onUserClick = { userId ->
                navController.navigate("profile/$userId")
            })
        }
    }
}