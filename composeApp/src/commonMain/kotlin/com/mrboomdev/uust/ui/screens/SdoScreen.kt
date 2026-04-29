package com.mrboomdev.uust.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.badlogic.gdx.math.collision.Ray

@Composable
fun SdoScreen(contentPadding: PaddingValues) {
    val context = LocalContext.current

    val webView = remember(context) {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
            }

            setWebViewClient(WebViewClient());

            loadUrl("https://isu.uust.ru/sdo_connect/")
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        factory = { webView }
    )
}