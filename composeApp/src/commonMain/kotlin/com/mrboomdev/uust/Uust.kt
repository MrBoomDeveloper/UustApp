package com.mrboomdev.uust

import com.mrboomdev.uust.utils.installCache
import io.ktor.client.*
import io.ktor.client.engine.*

internal expect fun getHttpEngine(): HttpClientEngineFactory<*>

object Uust {
    val httpClient by lazy {
        HttpClient(getHttpEngine()) {
            expectSuccess = true
            installCache()
        }
    }
}