package com.mrboomdev.uust.utils

import com.mrboomdev.uust.Uust
import com.mrboomdev.uust.context
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cache.storage.*

actual fun HttpClientConfig<CIOEngineConfig>.installCache() {
    install(HttpCache) {
        publicStorage(FileStorage(directory = Uust.context.cacheDir.resolve("http/public")))
        privateStorage(FileStorage(directory = Uust.context.cacheDir.resolve("http/private")))
    }
}