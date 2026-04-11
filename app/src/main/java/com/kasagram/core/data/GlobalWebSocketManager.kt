package com.kasagram.core.data


class GlobalWebSocketManager(
    authToken: String,
    private val onNotify: (String) -> Unit
) : BaseWebSocketManager(authToken) {

    override fun onMessageReceived(text: String) = onNotify(text)
    override fun onConnectionFailed(t: Throwable) { }
}