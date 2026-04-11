package com.kasagram.core

object Config {
    const val DEBUG_HOST = "10.0.2.2:8000"
    const val PROD_HOST = "kasagram.onrender.com"

    const val IS_DEBUG = true

    private const val DEBUG_URL = "http://${DEBUG_HOST}/api/"
    private const val PROD_URL = "https://${PROD_HOST}/api/"

    val BASE_URL = if (IS_DEBUG) DEBUG_URL else PROD_URL
    val HOST_NAME = if (IS_DEBUG) DEBUG_HOST else PROD_HOST
}