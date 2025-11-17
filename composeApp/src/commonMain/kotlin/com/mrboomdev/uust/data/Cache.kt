package com.mrboomdev.uust.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

expect object Cache {
    suspend fun <T> save(
        fileName: String,
        serializer: KSerializer<T>,
        value: T
    )

    suspend fun <T> load(
        fileName: String,
        serializer: KSerializer<T>
    ): T?
}

suspend inline fun <reified T> Cache.save(
    name: String,
    value: T
) = save(name, serializer = serializer<T>(), value)

suspend inline fun <reified T> Cache.load(
    name: String,
) = load(name, serializer = serializer<T>())