package com.kasagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kasagram.auth.User
import com.kasagram.post.Post
import com.kasagram.post.PostFeed
import com.kasagram.ui.theme.KasagramTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
                        Column {
                            // Твій Хедер
                            Row(
                                modifier = Modifier
                                    .padding(16.dp) // Додай відступи, щоб текст не лип до країв
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "For you",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(16.dp)) // Відступ між словами
                                Text(
                                    text = "Following",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }

                            // Лінія-розділювач (опціонально, як в інсті)
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                            // Тепер список постів піде ПІД хедером
                            PostFeed(mockPosts)
                        }
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
