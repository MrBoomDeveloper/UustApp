package com.mrboomdev.uust.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*

expect fun HttpClientConfig<CIOEngineConfig>.installCache()