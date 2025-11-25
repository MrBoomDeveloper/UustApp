package com.mrboomdev.uust.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun GeoMap(
    modifier: Modifier = Modifier,
    me: Pair<Double, Double>? = null
)