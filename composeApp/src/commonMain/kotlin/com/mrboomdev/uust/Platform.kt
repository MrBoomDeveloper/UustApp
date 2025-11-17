package com.mrboomdev.uust

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform