package com.mrboomdev.uust.utils

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

interface LocationManager {
    val isEnabledFlow: StateFlow<Boolean>
    val isPreciseFlow: StateFlow<Boolean>
    val locationFlow: StateFlow<Pair<Double, Double>?>
    val rotationFlow: StateFlow<Float>
}

@Composable
expect fun rememberLocationManager(): LocationManager