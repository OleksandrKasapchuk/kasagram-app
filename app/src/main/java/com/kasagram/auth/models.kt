package com.kasagram.auth

import com.kasagram.post.Post
import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Int,
    var username: String,
    var bio: String? = null,
    var avatarUrl: String? = null,
    var isOnline: Boolean = false,

    var firstName: String? = null,
    var lastName: String? = null,

    var lastSeen: String? = null,

    val userPosts: List<Post> = emptyList(),
    val postsCount: Int = 0
)


@Serializable
data class Subscription(
    val id: Int,
    val userFrom: User, // Хто підписався
    val userTo: User, // На кого підписався
    val created: String
)