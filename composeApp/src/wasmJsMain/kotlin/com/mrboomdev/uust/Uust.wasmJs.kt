package com.mrboomdev.uust

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

internal actual fun getHttpEngine(): HttpClientEngineFactory<*> = Js

actual val Uust.platform: Platform
    get() = Platform.WEB