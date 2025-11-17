package com.mrboomdev.uust.utils

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun BackEffect(onBack: () -> Unit) {
    BackHandler(enabled = true) { 
        onBack()
    }
}