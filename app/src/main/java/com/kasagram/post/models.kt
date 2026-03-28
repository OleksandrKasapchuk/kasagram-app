package com.kasagram.post

import com.kasagram.auth.User


data class Post(
    val id: Int,
    val user: User, // Використовуємо клас, що стоїть вище
    var content: String?,
    var media_url: String?,
    var likes_count: Int,
    var is_liked: Boolean,
    val date_published: String
)

data class Comment(
    val id: Int,
    val user: User,
    val content: String,
    val datePublished: String,
    val replies: List<Comment> = emptyList(),
    val parentId: Int? = null
)