package com.mrboomdev.uust.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun GeoMap(
    modifier: Modifier,
    originPosition: Pair<Double, Double>,
    myPosition: Pair<Double, Double>?
) = SimpleGeoMap(
    modifier = modifier,
    originPosition = originPosition,
    myPosition = myPosition
)