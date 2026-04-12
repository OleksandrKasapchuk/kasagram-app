package com.kasagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kasagram.auth.authGraph
import com.kasagram.auth.data.AuthSession
import com.kasagram.chat.chatGraph
import com.kasagram.core.ui.NavItem.Add.KasagramBottomBar
import com.kasagram.core.viewmodel.GlobalViewModel
import com.kasagram.notification.notificationGraph
import com.kasagram.post.postGraph
import com.kasagram.ui.theme.KasagramTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthSession.init(applicationContext)
        setContent {
            KasagramTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "index"

                val globalViewModel: GlobalViewModel = viewModel()

                LaunchedEffect(AuthSession.isLoggedIn) {
                    if (AuthSession.isLoggedIn) {
                        AuthSession.token?.let { globalViewModel.connect(it) }
                    } else {
                        globalViewModel.disconnect()
                    }
                }

                // 2. КАРКАС (SCAFFOLD)
                Scaffold(
                    bottomBar = {
                        KasagramBottomBar(
                            isAuthenticated = AuthSession.isLoggedIn,
                            unreadCount = 0,
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                if (route == "logout") {
                                    AuthSession.logout()
                                    navController.navigate("login") { popUpTo(0) }
                                } else {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
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
                            chatGraph(navController, globalViewModel)
                            authGraph(navController, onLogout = { // Створюємо одну спільну логіку
                                AuthSession.logout()
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}
