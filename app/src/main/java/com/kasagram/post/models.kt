package com.kasagram.post

import com.kasagram.auth.User
import kotlinx.serialization.Serializable


@Serializable
data class Post(
    val hashId: String,
    val user: User,
    var content: String?,

    val comments: List<Comment> = emptyList(),

    var mediaUrl: String,
    var likesCount: Int,
    var isLiked: Boolean,
    val datePublished: String
)

@Serializable
data class Comment(
    val id: Int,
    val user: User,
    val content: String,
    val replies: List<Comment> = emptyList(),

    val datePublished: String,

    val parentId: Int? = null
)