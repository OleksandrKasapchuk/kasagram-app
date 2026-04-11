package com.kasagram.core.data


import com.kasagram.auth.data.AuthApi
import com.kasagram.auth.data.AuthSession
import com.kasagram.chat.data.ChatApi
import com.kasagram.core.Config
import com.kasagram.notification.data.NotificationApi
import com.kasagram.post.data.PostApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


object RetrofitClient {

    val json = Json {
        ignoreUnknownKeys = true // НЕ падати, якщо бекенд прислав нове поле
        coerceInputValues = true // Підставляти default values, якщо прийшов null або поле відсутнє
        isLenient = true         // Бути лояльним до нестандартних форматів
    }
    private val contentType = "application/json".toMediaType()

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = AuthSession.token

        val requestBuilder = originalRequest.newBuilder()

        if (AuthSession.isLoggedIn) {
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
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val postApi: PostApi by lazy { retrofit.create(PostApi::class.java) }
    val сhatApi: ChatApi by lazy { retrofit.create(ChatApi::class.java) }
    val notificationApi: NotificationApi by lazy { retrofit.create(NotificationApi::class.java) }
}


@Serializable
data class PaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)