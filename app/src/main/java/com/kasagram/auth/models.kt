package com.kasagram.auth

data class User(
    val id: Int,
    var username: String,
    var avatar_url: String?,
    var bio: String?,
    var is_online: Boolean,
    var first_name: String,
    var last_name: String,
    var last_seen: String?
)

data class Subscription(
    val id: Int,
    val user_from: User, // Хто підписався
    val user_to: User, // На кого підписався
    val created: String
)