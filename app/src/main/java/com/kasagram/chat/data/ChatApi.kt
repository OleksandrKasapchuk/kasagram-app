package com.kasagram.chat.data

import com.kasagram.PaginatedResponse
import com.kasagram.chat.Chat
import com.kasagram.chat.Message
import retrofit2.http.GET
import retrofit2.http.Query


interface ChatApi {
    @GET("chats/")
    suspend fun getChats(@Query("page") page: Int): PaginatedResponse<Chat>

    @GET("messages/{chatId}/")
    suspend fun getMessages(): PaginatedResponse<Message>
}