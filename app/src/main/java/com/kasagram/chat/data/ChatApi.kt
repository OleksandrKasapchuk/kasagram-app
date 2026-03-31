package com.kasagram.chat.data

import com.kasagram.PaginatedResponse
import com.kasagram.auth.User
import com.kasagram.chat.Chat
import com.kasagram.chat.Message
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ChatApi {
    @GET("chats/")
    suspend fun getChats(@Query("page") page: Int): PaginatedResponse<Chat>

    @GET("messages/{chatId}/")
    suspend fun getMessages(@Path("chatId") id: Int,
                            @Query("oldest_id") oldestId: Int? = null
    ): MessageResponse
}

data class MessageResponse(
    val success: Boolean,
    val participant: User,
    val messages: List<Message>
)