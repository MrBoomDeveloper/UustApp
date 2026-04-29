package com.mrboomdev.uust.ui.widgets

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.toSize
import androidx.window.core.layout.WindowSizeClass
import kotlinx.serialization.json.Json.Default.configuration

//@OptIn(ExperimentalMaterial3AdaptiveApi::class)
//@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
//@Composable
//internal fun currentWindowSizeClass(): WindowAdaptiveInfo {
//    val context = androidx.glance.LocalContext.current
//    
//    val density = Density(
//        density = context.resources.displayMetrics.density, 
//        fontScale = context.resources.configuration.fontScale
//    )
//    
//    val windowSize = with(density) { 
//        currentWindowSize().toSize().toDpSize() 
//    }
//    
//    return WindowSizeClass.computeFromDpSize(windowSize)
//}