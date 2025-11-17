package com.mrboomdev.uust

import com.mrboomdev.uust.utils.installCache
import io.ktor.client.*
import io.ktor.client.engine.cio.*

object Uust {
    val httpClient by lazy {
        HttpClient(CIO) {
            expectSuccess = true
            installCache()
        }
    }
}