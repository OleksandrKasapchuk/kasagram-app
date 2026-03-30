package com.kasagram

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kasagram.auth.User
import com.kasagram.post.Post
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @POST("login/")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register/")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("posts/")
    suspend fun getPosts(@Query("page") page: Int): PaginatedResponse<Post>

    @GET("posts/{id}/")
    suspend fun getPostDetail(@Path("id") id: Int): Post

    @GET("user-info/{id}/")
    suspend fun getUserDetail(@Path("id") id: Int): User
}


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

    val instance: ApiService by lazy {
        val baseUrl = if (IS_DEBUG) DEBUG_URL else PROD_URL

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}


data class PaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String,
    val bio: String
)

data class AuthResponse(
    val token: String,
    val user_id: Int,
    val username: String
)

object AuthSession {
    private const val PREFS_NAME = "kasagram_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"

    private lateinit var prefs: android.content.SharedPreferences

    // Реактивні стейти для Compose
    var token by mutableStateOf<String?>(null)
        private set
    var userId by mutableStateOf(-1)
        private set

    fun init(context: android.content.Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
        token = prefs.getString(KEY_TOKEN, null)
        userId = prefs.getInt(KEY_USER_ID, -1)
    }

    fun updateSession(newToken: String?, newUserId: Int) {
        token = newToken
        userId = newUserId
        prefs.edit().apply {
            putString(KEY_TOKEN, newToken)
            putInt(KEY_USER_ID, newUserId)
            apply()
        }
    }

    val isLoggedIn: Boolean
        get() = !token.isNullOrEmpty()

    fun logout() {
        token = null
        userId = -1
        prefs.edit().clear().apply()
    }
}