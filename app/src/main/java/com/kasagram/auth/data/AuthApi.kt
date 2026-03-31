package com.kasagram.auth.data

import com.kasagram.auth.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface AuthApi {
    @POST("login/")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register/")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("user-info/{id}/")
    suspend fun getUserDetail(@Path("id") id: Int): User
}