package com.mrboomdev.uust.utils

import androidx.compose.ui.Modifier

inline fun Modifier.ifThen(condition: Boolean, block: Modifier.() -> Modifier): Modifier {
    return if(condition) block() else this
}