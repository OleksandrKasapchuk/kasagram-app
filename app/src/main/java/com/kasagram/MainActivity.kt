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
                media_url = null,
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
            KasagramTheme {
                // Викликаємо нашу стрічку і передаємо список
                PostFeed(posts = mockPosts)
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
