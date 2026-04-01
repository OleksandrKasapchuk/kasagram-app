package com.kasagram.post.data

import com.kasagram.PaginatedResponse
import com.kasagram.post.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface PostApi {
    @GET("posts/")
    suspend fun getPosts(@Query("page") page: Int): PaginatedResponse<Post>

    @GET("posts/{id}/")
    suspend fun getPostDetail(@Path("id") id: Int): Post

    @Multipart
    @POST("posts/create/")
    suspend fun createPost(
        @Part("content") content: RequestBody,
        @Part media: MultipartBody.Part
    ): Post
}