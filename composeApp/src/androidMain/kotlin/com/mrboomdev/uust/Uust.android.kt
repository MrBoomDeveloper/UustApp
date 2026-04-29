package com.mrboomdev.uust

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class UustApplication : Application() {
    init {
        mContext = this
    }
}

@SuppressLint("StaticFieldLeak")
private var mContext: Context? = null

fun Uust.setContext(context: Context) {
    mContext = context.applicationContext
}

val Uust.context: Context get() = mContext!!

actual val Uust.platform: Platform
    get() = Platform.ANDROID