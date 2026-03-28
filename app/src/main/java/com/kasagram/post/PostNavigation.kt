package com.kasagram.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kasagram.mockPosts


fun NavGraphBuilder.postGraph(navController: NavController) {
    composable("index") {
        Index(
            posts = mockPosts,
            onUserClick = { userId -> navController.navigate("profile/$userId")})
    }

    composable("add_post") {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Тут буде створення поста", color = MaterialTheme.colorScheme.primary)
        }
    }
}
