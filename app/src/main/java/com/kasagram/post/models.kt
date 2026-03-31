package com.kasagram.post

import com.google.gson.annotations.SerializedName
import com.kasagram.auth.User


data class Post(
    val id: Int,
    val user: User,
    var content: String?,

    @SerializedName("media_url") // Те, що приходить від Django
    var mediaUrl: String,       // Те, що ти використовуєш у Kotlin

    @SerializedName("likes_count")
    var likesCount: Int,

    @SerializedName("is_liked")
    var isLiked: Boolean,

    @SerializedName("date_published")
    val datePublished: String
)

data class Comment(
    val id: Int,
    val user: User,
    val content: String,
    val replies: List<Comment> = emptyList(),

    @SerializedName("date_published")
    val datePublished: String,

    @SerializedName("parent_id")
    val parentId: Int? = null
)