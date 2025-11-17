package com.mrboomdev.uust.uust

import com.mrboomdev.uust.Platform

class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()