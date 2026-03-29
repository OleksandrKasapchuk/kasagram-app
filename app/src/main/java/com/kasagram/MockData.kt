package com.kasagram


import com.kasagram.auth.User
import com.kasagram.chat.Chat
import com.kasagram.chat.Message
import com.kasagram.notification.Notification
import com.kasagram.post.Post


val author = User(
    id = 1,
    username = "Sashapre228",
    avatarUrl = "https://res.cloudinary.com/ddothsprl/image/upload/v1773921665/sngrtl1d064awzlzwl7h.png",
    bio = "Popa jopa",
    isOnline = true,
    firstName = "Oleksandr",
    lastName = "Kasapchuk",
    lastSeen = "now"
)


val author2 = User(
    id = 2,
    username = "test",
    avatarUrl = null,
    bio = "Jopa popa",
    isOnline = false,
    firstName = "Test",
    lastName = "Testing",
    lastSeen = "long time ago"
)

val author3 = User(
    id = 3,
    username = "test34",
    avatarUrl = "https://res.cloudinary.com/ddothsprl/image/upload/v1774601550/t1jy8z11ldrr8q2bingc.webp",
    bio = "Jopa popa",
    isOnline = false,
    firstName = "grrr",
    lastName = "dflaskf",
    lastSeen = "long time ago"
)

val mockPosts = mutableListOf(
    Post(
        id = 1,
        user = author,
        content = "Перший пост у Kasagram!",
        mediaUrl = "https://res.cloudinary.com/ddothsprl/image/upload/v1774604052/iifxgqt1scbnfoszdg98.jpg",
        likesCount = 10,
        isLiked = false,
        datePublished = "now"
    ),
//    Post(
//        id = 2,
//        user = author,
//        content = "Django + Kotlin = ❤️",
//        mediaUrl = "https://res.cloudinary.com/ddothsprl/image/upload/v1773770853/kznjxvvqcllmznlmgple.png",
//        likes_count = 42,
//        is_liked = true,
//        date_published = "1 hour ago"
//    ),
//    Post(
//        id = 3,
//        user = author,
//        content = "test ❤️",
//        mediaUrl = "https://res.cloudinary.com/ddothsprl/image/upload/v1773770521/pjrr1ac5ddfrzv1aoxxx.jpg",
//        likes_count = 20,
//        is_liked = true,
//        date_published = "2 hours ago"
//    ),
//    Post(
//        id = 4,
//        user = author2,
//        content = "author 2 post️",
//        mediaUrl = "https://res.cloudinary.com/ddothsprl/image/upload/v1774370736/r51fd2yiqepoysdneem2.png",
//        likes_count = 20,
//        is_liked = false,
//        date_published = "4 hours ago"
//    ),
//    Post(
//        id = 5,
//        user = author,
//        content = "test ❤️",
//        mediaUrl = "https://res.cloudinary.com/ddothsprl/image/upload/v1773771535/re5lgqjuxb4tzrnvdbt9.png",
//        likesCount = 20,
//        is_liked = true,
//        date_published = "2 hours ago"
//    )
)

object AuthSession {
    var currentUser: User? = author
    var token: String? = "afafx"
}

val chatList = listOf (
    Chat(
        id = 1,
        participants = listOf(author, author3),
        created = "01.01.01",
    ),

    Chat(
        id = 2,
        participants = listOf(author, author2),
        created = "03.03.03",
    )
)

val messagesList = listOf (
    Message(chatList[1], author, "Hello!", "01.01.01", true, null),
    Message(chatList[1], author2, "test user message!", "01.01.01", true, null),
    Message(chatList[1], author, "test!", "01.01.01", true, null),
)

val notificationList = listOf (
    Notification(
        id = 1,
        actor = author3,
        message = "sent you a message, View",
        targetUrl = "chat/1",
        timestamp = "now"
    ),
    Notification(
        id = 2,
        actor = author2,
        message = "liked your post, View",
        targetUrl = "post/1",
        timestamp = "now"
    ),
    Notification(
        id = 3,
        actor = author3,
        message = "commented your post, View",
        targetUrl = "post/1",
        timestamp = "now"
    )
)