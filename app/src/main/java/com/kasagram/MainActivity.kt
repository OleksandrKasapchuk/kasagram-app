package com.kasagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kasagram.auth.ProfileScreen
import com.kasagram.chat.ChatListScreen
import com.kasagram.post.Index
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
                            isAuthenticated = AuthSession.isAuthenticated,
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
                    // Вказуємо колір фону для самого Scaffold, щоб не було білих плям
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    // 3. ПОВЕРХНЯ (SURFACE) ТА КОНТЕНТ
                    // innerPadding — це відступ, який Scaffold дає контенту,
                    // щоб BottomBar не перекривав нижній пост.
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "index", // Початкова сторінка (як '/')
                        ) {
                            // Описуємо маршрути:
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
                            composable("chat_list") {
                                ChatListScreen(author, chatList)
                            }

                            composable("notifications") {
                                Text("Тут сповіщення", color = MaterialTheme.colorScheme.primary)
                            }

                            composable(
                                route = "profile/{userId}",
                                arguments = listOf(navArgument("userId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                // 1. Дістаємо ID з аргументів маршруту
                                val userId = backStackEntry.arguments?.getInt("userId") ?: author.id

                                // 2. Шукаємо потрібного юзера в наших моках
                                // (Припустимо, у тебе є список mockUsers, або шукаємо серед авторів постів)
                                val userToShow = mockPosts.find { it.user.id == userId }?.user ?: author

                                // 3. Фільтруємо пости саме для цього юзера
                                val userPosts = mockPosts.filter { it.user.id == userId }

                                // 4. Віддаємо дані в екран
                                ProfileScreen(user = userToShow, userPosts = userPosts)
                            }
                        }
                    }
                }
            }
        }
    }
}
