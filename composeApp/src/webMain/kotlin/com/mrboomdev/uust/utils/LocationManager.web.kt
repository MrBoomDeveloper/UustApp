package com.mrboomdev.uust.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
actual fun rememberLocationManager(): LocationManager {
    return remember { 
        object : LocationManager {
            // TODO: Implement methods from below
            
            override val isEnabledFlow: StateFlow<Boolean>
                get() = MutableStateFlow(false)
            
            override val isPreciseFlow: StateFlow<Boolean>
                get() = MutableStateFlow(false)
            
            override val locationFlow: StateFlow<Pair<Double, Double>?>
                get() = MutableStateFlow(null)
            
            override val rotationFlow: StateFlow<Float>
                get() = MutableStateFlow(0f)
        }
    }
}