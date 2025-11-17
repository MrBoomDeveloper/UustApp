package com.mrboomdev.uust.uust

import com.mrboomdev.uust.Platform

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()