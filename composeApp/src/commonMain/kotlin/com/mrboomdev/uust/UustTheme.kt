package com.mrboomdev.uust

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import uust.composeapp.generated.resources.*

object UustTheme {
    fun darkColorScheme() = androidx.compose.material3.darkColorScheme(
        primary = Color(0xFF794BF6),
        onPrimary = Color.White,
        secondary = Color(0xFFcbb8ff),
        onSurfaceVariant = Color(0xFF737187),
        secondaryContainer = Color(0xFFBDA8E7),
        onSecondaryContainer = Color(0xFF1A005A)
    )
    
    fun lightColorScheme() = androidx.compose.material3.lightColorScheme(
        primary = Color(0xff4d19cc),
        secondary = Color(0xFF6340BA)
    )
    
    object fonts {
        @get:Composable
        val golos get() = FontFamily(
            Font(Res.font.golos_text_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
            Font(Res.font.golos_text_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
            Font(Res.font.golos_text_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
            Font(Res.font.golos_text_black, weight = FontWeight.Black, style = FontStyle.Normal),
        )
    }
}