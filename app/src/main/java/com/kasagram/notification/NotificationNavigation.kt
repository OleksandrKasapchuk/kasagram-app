package com.kasagram.notification

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kasagram.notificationList


fun NavGraphBuilder.notificationGraph(navController: NavController) {
    composable("notifications") {
        NotificationListScreen(notificationList,
            onUserClick = {userId -> navController.navigate("profile/${userId}") },
            onNotificationClick = {})
    }
}