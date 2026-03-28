package com.kasagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kasagram.auth.authGraph
import com.kasagram.chat.chatGraph
import com.kasagram.notification.notificationGraph
import com.kasagram.post.postGraph
import com.kasagram.ui.theme.KasagramTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KasagramTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "index"
                // 2. КАРКАС (SCAFFOLD)
                Scaffold(
                    bottomBar = {
                        KasagramBottomBar(
                            isAuthenticated = !AuthSession.token?.isEmpty()!!,
                            unreadCount = 5,
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    // Щоб не накопичувати купу сторінок в пам'яті:
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "index",
                        ) {
                            notificationGraph(navController)
                            postGraph(navController)
                            chatGraph(navController)
                            authGraph(navController)
                        }
                    }
                }
            }
        }
    }
}
