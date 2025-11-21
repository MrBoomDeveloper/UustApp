package com.mrboomdev.uust

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.mrboomdev.uust.navigation.Routes

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        App(Routes.Home)
    }
}