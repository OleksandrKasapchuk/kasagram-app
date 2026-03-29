package com.kasagram.auth

import com.google.gson.annotations.SerializedName
import com.kasagram.post.Post

data class User(
    val id: Int,
    var username: String,
    var bio: String?,

    @SerializedName("avatar_url")
    var avatarUrl: String?,

    @SerializedName("is_online")
    var isOnline: Boolean,

    @SerializedName("first_name")
    var firstName: String,

    @SerializedName("last_name")
    var lastName: String,

    @SerializedName("last_seen")
    var lastSeen: String?,

    @SerializedName("user_posts")
    val userPosts: List<Post> = emptyList(), // Список постів від бекенда
    @SerializedName("posts_count")
    val postsCount: Int = 0
)

data class Subscription(
    val id: Int,
    @SerializedName("user_from")
    val userFrom: User, // Хто підписався
    @SerializedName("user_to")
    val userTo: User, // На кого підписався
    val created: String
)