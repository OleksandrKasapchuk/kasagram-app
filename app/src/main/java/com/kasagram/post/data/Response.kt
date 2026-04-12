package com.kasagram.post.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LikeResponse(
    val liked: Boolean,
    @SerialName("likes_count") val likesCount: Int
)