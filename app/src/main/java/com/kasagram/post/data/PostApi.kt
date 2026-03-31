package com.kasagram.post.data

import com.kasagram.PaginatedResponse
import com.kasagram.post.Post
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PostApi {
    @GET("posts/")
    suspend fun getPosts(@Query("page") page: Int): PaginatedResponse<Post>

    @GET("posts/{id}/")
    suspend fun getPostDetail(@Path("id") id: Int): Post
}