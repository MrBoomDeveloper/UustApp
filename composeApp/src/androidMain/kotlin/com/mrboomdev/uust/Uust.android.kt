package com.mrboomdev.uust

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
private var _context: Context? = null

fun Uust.setContext(context: Context) {
    _context = context.applicationContext
}

val Uust.context: Context get() = _context!!

actual val Uust.platform: Platform
    get() = Platform.ANDROID