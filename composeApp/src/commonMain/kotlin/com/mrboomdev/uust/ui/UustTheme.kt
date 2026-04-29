package com.mrboomdev.uust.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.mrboomdev.uust.data.settings.UustSettings
import com.mrboomdev.uust.data.settings.observeAsState
import com.mrboomdev.uust.resources.Res
import com.mrboomdev.uust.resources.golos_text_black
import com.mrboomdev.uust.resources.golos_text_bold
import com.mrboomdev.uust.resources.golos_text_medium
import com.mrboomdev.uust.resources.golos_text_regular
import org.jetbrains.compose.resources.Font

@Composable
fun isDarkTheme(): Boolean {
    val isSystemDark = isSystemInDarkTheme()
    val preferenceValue by UustSettings.themeMode.observeAsState()
    
    return when(preferenceValue) {
        UustSettings.ThemeMode.SYSTEM -> isSystemDark
        UustSettings.ThemeMode.LIGHT -> false
        UustSettings.ThemeMode.DARK -> true
    }
}

@Composable
fun UustTheme(
    isDarkTheme: Boolean = isDarkTheme(),
    content: @Composable () -> Unit
) = MaterialTheme(
    colorScheme = if(isDarkTheme) {
        UustTheme.darkColorScheme()
    } else UustTheme.lightColorScheme(),
    
    content = content
)

object UustTheme {
    fun darkColorScheme() = androidx.compose.material3.darkColorScheme(
        primary = Color(0xFF966FFF),
        primaryContainer = Color(0xFF6221C9),
        onPrimary = Color.Black,
        secondary = Color(0xFFcbb8ff),
        onSurfaceVariant = Color(0xFF737187),
        secondaryContainer = Color(0xFFBDA8E7),
        onSecondaryContainer = Color(0xFF1A005A),
        background = Color(0xFF0D071A),
        surface = Color(0xFF110823),
        surfaceContainer = Color(0xFF281742),
        surfaceContainerLow = Color(0xFF120C1A)
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