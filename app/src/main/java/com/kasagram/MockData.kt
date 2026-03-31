package com.kasagram


import com.kasagram.auth.User
import com.kasagram.notification.Notification



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