package com.kasagram.core

import kotlinx.serialization.json.*


fun JsonElement.int(key: String): Int? = this.jsonObject[key]?.jsonPrimitive?.intOrNull
fun JsonElement.bool(key: String): Boolean = this.jsonObject[key]?.jsonPrimitive?.boolean ?: false
fun JsonElement.str(key: String): String = this.jsonObject[key]?.jsonPrimitive?.content ?: ""