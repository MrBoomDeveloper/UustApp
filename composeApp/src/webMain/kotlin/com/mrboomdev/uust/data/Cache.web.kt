package com.mrboomdev.uust.data

import kotlinx.serialization.KSerializer

actual object Cache {
    private val values = mutableMapOf<String, Any>()
    
    actual suspend fun <T> save(fileName: String, serializer: KSerializer<T>, value: T) {
        values[fileName] = value as Any
    }

    actual suspend fun <T> load(fileName: String, serializer: KSerializer<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return values[fileName] as T?
    }
}