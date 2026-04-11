package com.kasagram.notification

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


fun NavGraphBuilder.notificationGraph(navController: NavController) {
    composable("notifications") {

        NotificationListScreen(
            onUserClick = {userId -> navController.navigate("profile/${userId}") },
            onNotificationClick = {})
    }
}