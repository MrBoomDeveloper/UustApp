package com.mrboomdev.uust.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A 3d geolocation-based map.
 *
 * @param modifier The modifier to be applied to the map component.
 * @param originPosition All objects are being placed relative to this position.
 * @param myPosition The user's position
 */
@Composable
expect fun GeoMap(
    modifier: Modifier = Modifier,
    originPosition: Pair<Double, Double>,
    myPosition: Pair<Double, Double>? = null,
//    onDrag: (Pair<Double, Double>) -> Boolean = { false }
)