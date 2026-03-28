package com.kasagram

import com.kasagram.auth.User
import com.kasagram.chat.Chat
import com.kasagram.chat.Message
import com.kasagram.post.Post

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

val author3 = User(
    id = 3,
    username = "test34",
    avatar_url = "https://res.cloudinary.com/ddothsprl/image/upload/v1774601550/t1jy8z11ldrr8q2bingc.webp",
    bio = "Jopa popa",
    is_online = false,
    first_name = "grrr",
    last_name = "dflaskf",
    last_seen = "long time ago"
)

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

object AuthSession {
    var currentUser: User = author
    var isAuthenticated: Boolean = true
    var token: String = "dssw"
}

val chatList = listOf (
    Chat(
        id=1,
        participants = listOf(author, author3),
        created="01.01.01",
    ),

    Chat(
        id=2,
        participants = listOf(author, author2),
        created="03.03.03",
    )
)

val messagesList = listOf (
    Message(chatList[1], author, "Hello!", "01.01.01", true, null),
    Message(chatList[1], author2, "test user message!", "01.01.01", true, null),
    Message(chatList[1], author, "test!", "01.01.01", true, null),
)