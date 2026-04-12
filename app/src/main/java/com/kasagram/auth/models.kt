package com.kasagram.auth

import com.kasagram.post.Post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Int,
    var username: String,
    var bio: String? = null,

    @SerialName("avatar_url")
    var avatarUrl: String? = null,

    @SerialName("is_online")
    var isOnline: Boolean = false,

    @SerialName("first_name")
    var firstName: String? = null,

    @SerialName("last_name")
    var lastName: String? = null,

    @SerialName("last_seen")
    var lastSeen: String? = null,

    @SerialName("user_posts")
    val userPosts: List<Post> = emptyList(),
    @SerialName("posts_count")
    val postsCount: Int = 0
)


@Serializable
data class Subscription(
    val id: Int,
    @SerialName("user_from")
    val userFrom: User, // Хто підписався
    @SerialName("user_to")
    val userTo: User, // На кого підписався
    val created: String
)