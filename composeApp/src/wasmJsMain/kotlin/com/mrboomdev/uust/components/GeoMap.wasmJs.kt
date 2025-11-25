package com.mrboomdev.uust.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun GeoMap(
    modifier: Modifier, 
    me: Pair<Double, Double>?
) {
    Box(modifier) {
        Text("GeoMap isn't implemented yet on web!")
    }
}