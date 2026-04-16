package com.kasagram.post.data

import kotlinx.serialization.Serializable


@Serializable
data class LikeResponse(
    val liked: Boolean, val likesCount: Int
)