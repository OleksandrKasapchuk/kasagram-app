package com.kasagram

import com.kasagram.auth.data.AuthApi
import com.kasagram.auth.data.AuthSession
import com.kasagram.chat.data.ChatApi
import com.kasagram.post.data.PostApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val DEBUG_URL = "http://10.0.2.2:8000/api/"
    private const val PROD_URL = "https://kasagram.onrender.com/api/"

    private const val IS_DEBUG = true

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = AuthSession.token

        val requestBuilder = originalRequest.newBuilder()

        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Token $token")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    val retrofit: Retrofit by lazy {
        val baseUrl = if (IS_DEBUG) DEBUG_URL else PROD_URL

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val postApi: PostApi by lazy { retrofit.create(PostApi::class.java) }
    val сhatApi: ChatApi by lazy { retrofit.create(ChatApi::class.java) }
}


data class PaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)