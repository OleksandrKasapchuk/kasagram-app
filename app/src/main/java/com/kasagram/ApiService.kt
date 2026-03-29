package com.kasagram

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

    @GET("ping/")
    suspend fun getPing(): PingResponse

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

    private const val IS_DEBUG = true // Потім зміниш на false перед релізом

    // 1. Створюємо перехоплювач, який лізе в твій AuthSession
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = AuthSession.token // Беремо токен прямо з твого об'єкта

        val requestBuilder = originalRequest.newBuilder()

        // Якщо токен є — додаємо заголовок, який чекає Django
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Token $token")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    // 2. Налаштовуємо клієнт з цим перехоплювачем
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        // Додамо логування, щоб ти бачив у консолі, що летить на сервер
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    val instance: ApiService by lazy {
        val baseUrl = if (IS_DEBUG) DEBUG_URL else PROD_URL

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient) // ПІДКЛЮЧАЄМО КЛІЄНТ ТУТ
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

// Те, що ми шлемо на сервер
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

// Те, що Django (наш CustomAuthToken) віддає у відповідь
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

    // Ініціалізуємо один раз при запуску додатка
    fun init(context: android.content.Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
    }

    // Токен (читаємо з пам'яті, пишемо в пам'ять)
    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    // ID поточного юзера
    var userId: Int
        get() = prefs.getInt(KEY_USER_ID, -1)
        set(value) = prefs.edit().putInt(KEY_USER_ID, value).apply()

    // Перевірка, чи ми в системі
    val isLoggedIn: Boolean
        get() = token != null

    fun logout() {
        prefs.edit().clear().apply()
    }
}