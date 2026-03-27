package com.kasagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kasagram.auth.User
import com.kasagram.post.Post
import com.kasagram.post.PostFeed
import com.kasagram.ui.theme.KasagramTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val author = User(
            id = 1,
            username = "sasha",
            avatar_url = null,
            bio = "Android Developer",
            is_online = true,
            first_name = "Олександр",
            last_name = "К."
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
                date_published = "щойно"
            ),
            Post(
                id = 2,
                user = author,
                content = "Django + Kotlin = ❤️",
                media_url = null,
                likes_count = 42,
                is_liked = true,
                date_published = "1 годину тому"
            )
        )

        setContent {
            // 1. НАЙЦЕНТРАЛЬНІША МАТРЬОШКА - ТЕМА
            // Вона каже всім всередині: "Ми використовуємо кольори BgDark та AccentRed"
            KasagramTheme {

                // 2. КАРКАС (SCAFFOLD)
                // Він каже: "Я тримаю BottomBar знизу, а контент посередині"
                Scaffold(
                    bottomBar = {
                        KasagramBottomBar(
                            isAuthenticated = true,
                            unreadCount = 5,
                            currentRoute = "index",
                            onNavigate = { route -> println("Йдемо на $route") }
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
                        // Викликаємо твою стрічку постів
                        PostFeed(mockPosts)
                    }
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KasagramTheme {
        Greeting("Android")
    }
}
