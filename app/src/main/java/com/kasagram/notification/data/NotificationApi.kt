package com.kasagram.notification.data

import com.kasagram.notification.Notification
import retrofit2.http.GET


interface NotificationApi {

    @GET("notifications/")
    suspend fun getNotifications(): List<Notification>

}