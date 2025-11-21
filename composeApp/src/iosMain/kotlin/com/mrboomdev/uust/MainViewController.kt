package com.mrboomdev.uust

import androidx.compose.ui.window.ComposeUIViewController
import com.mrboomdev.uust.navigation.Routes

fun MainViewController() = ComposeUIViewController { 
    App(Routes.Home) 
}