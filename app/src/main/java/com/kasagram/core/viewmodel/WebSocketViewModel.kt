package com.kasagram.core.viewmodel

import com.kasagram.core.data.BaseWebSocketManager


abstract class SocketViewModel<T : BaseWebSocketManager> : BaseViewModel() {
    protected var wsManager: T? = null

    // Кожна ViewModel сама вирішить, як обробляти сирий JSON
    abstract fun handleIncomingEvent(jsonString: String)

    fun disconnect() {
        wsManager?.disconnect()
    }

    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}