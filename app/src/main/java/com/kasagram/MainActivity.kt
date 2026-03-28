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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kasagram.auth.ProfileScreen
import com.kasagram.auth.User
import com.kasagram.post.Index
import com.kasagram.post.Post
import com.kasagram.ui.theme.KasagramTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val author = User(
            id = 1,
            username = "Sashapre228",
            avatar_url = "https://res.cloudinary.com/ddothsprl/image/upload/v1773921665/sngrtl1d064awzlzwl7h.png",
            bio = "Popa jopa",
            is_online = true,
            first_name = "Oleksandr",
            last_name = "Kasapchuk",
            last_seen = "now"
        )

        val author2 = User(
            id = 2,
            username = "test",
            avatar_url = null,
            bio = "Jopa popa",
            is_online = false,
            first_name = "Test",
            last_name = "Testing",
            last_seen = "long time ago"
        )

        // Тепер створюємо список постів з реальними даними
        val mockPosts = listOf(
            Post(
                id = 1,
                user = author,
                content = "Перший пост у Kasagram!",
                media_url = "https://res.cloudinary.com/ddothsprl/image/upload/v1774604052/iifxgqt1scbnfoszdg98.jpg",
                likes_count = 10,
                is_liked = false,
                date_published = "now"
            ),
            Post(
                id = 2,
                user = author,
                content = "Django + Kotlin = ❤️",
                media_url = "https://res.cloudinary.com/ddothsprl/image/upload/v1773770853/kznjxvvqcllmznlmgple.png",
                likes_count = 42,
                is_liked = true,
                date_published = "1 hour ago"
            ),
            Post(
                id = 3,
                user = author,
                content = "test ❤️",
                media_url = "https://res.cloudinary.com/ddothsprl/image/upload/v1773770521/pjrr1ac5ddfrzv1aoxxx.jpg",
                likes_count = 20,
                is_liked = true,
                date_published = "2 hours ago"
            ),
            Post(
                id = 4,
                user = author2,
                content = "author 2 post️",
                media_url = "https://res.cloudinary.com/ddothsprl/image/upload/v1774370736/r51fd2yiqepoysdneem2.png",
                likes_count = 20,
                is_liked = false,
                date_published = "4 hours ago"
            ),
            Post(
                id = 5,
                user = author,
                content = "test ❤️",
                media_url = "https://res.cloudinary.com/ddothsprl/image/upload/v1773771535/re5lgqjuxb4tzrnvdbt9.png",
                likes_count = 20,
                is_liked = true,
                date_published = "2 hours ago"
            )
        )

        setContent {
            KasagramTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "index"
                // 2. КАРКАС (SCAFFOLD)
                Scaffold(
                    bottomBar = {
                        KasagramBottomBar(
                            isAuthenticated = true,
                            unreadCount = 5,
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                // ТЕПЕР ЦЕ ПРАЦЮЄ:
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
                               Index(posts = mockPosts)
                            }

                            composable("add_post") {
                                Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                                    Text("Тут буде створення поста", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                            composable("chat_list") {
                                Text("Тут чат", color = MaterialTheme.colorScheme.primary)
                            }

                            composable("notifications") {
                                Text("Тут сповіщення", color = MaterialTheme.colorScheme.primary)
                            }

                            composable("user-info") {
                                val userPosts = mockPosts.filter { post -> post.user.id == author.id }
                                ProfileScreen(user = author, userPosts = userPosts)
                            }

                        }
                    }
                }
            }
        }
    }
}
