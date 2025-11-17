package com.mrboomdev.uust.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WindowInsets.add(
    left: Dp = 0.dp, 
    top: Dp = 0.dp, 
    right: Dp = 0.dp, 
    bottom: Dp = 0.dp
) = WindowInsets(
    left = this.left + left,
    top = this.top + top,
    right = this.right + right,
    bottom = this.bottom + bottom
)

@get:Composable
val WindowInsets.left get() = with(LocalDensity.current) {
    getLeft(this, LocalLayoutDirection.current).toDp()
}

@get:Composable
val WindowInsets.right get() = with(LocalDensity.current) {
    getRight(this, LocalLayoutDirection.current).toDp()
}

@get:Composable
val WindowInsets.top get() = with(LocalDensity.current) {
    getTop(LocalDensity.current).toDp()
}

@get:Composable
val WindowInsets.bottom get() = with(LocalDensity.current) {
    getBottom(LocalDensity.current).toDp()
}