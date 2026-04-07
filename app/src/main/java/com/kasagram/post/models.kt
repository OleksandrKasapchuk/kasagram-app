package com.kasagram.post

import com.kasagram.auth.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Post(
    val id: Int,
    val user: User,
    var content: String?,

    val comments: List<Comment> = emptyList(),

    @SerialName("media_url")
    var mediaUrl: String,

    @SerialName("likes_count")
    var likesCount: Int,

    @SerialName("is_liked")
    var isLiked: Boolean,

    @SerialName("date_published")
    val datePublished: String
)

@Serializable
data class Comment(
    val id: Int,
    val user: User,
    val content: String,
    val replies: List<Comment> = emptyList(),

    @SerialName("date_published")
    val datePublished: String,

    @SerialName("parent_id")
    val parentId: Int? = null
)