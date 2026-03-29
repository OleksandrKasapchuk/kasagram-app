package com.kasagram

import com.kasagram.post.Post
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("ping/")
    suspend fun getPing(): PingResponse

    @GET("posts/")
    suspend fun getPosts(@Query("page") page: Int): PaginatedResponse<Post>

    // Додай цей метод пізніше, коли стрічка запрацює
    @GET("posts/{id}/")
    suspend fun getPostDetail(@Path("id") id: Int): Post
}


object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

data class PingResponse(
    val status: String
)

data class PaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)