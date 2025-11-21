package com.mrboomdev.uust

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

internal actual fun getHttpEngine(): HttpClientEngineFactory<*> = CIO