package com.mrboomdev.uust.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * A 2d geolocation-based map used for low budget hardware
 * and platforms where 3d map rendering isn't implemented yet.
 *
 * @param modifier The modifier to be applied to the map component.
 * @param originPosition All objects are being placed relative to this position.
 * @param myPosition The user's position
 */
@Composable
fun SimpleGeoMap(
    modifier: Modifier = Modifier,
    originPosition: Pair<Double, Double>,
    myPosition: Pair<Double, Double>? = null
) {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),

        color = Color.Red,
        text = "SimpleGeoMap not implemented yet!"
    )
}