package com.mrboomdev.uust.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.UustSettings
import com.mrboomdev.uust.observeAsState

@Composable
fun SettingsScreen(
    contentPadding: PaddingValues
) {
    val theme by UustSettings.themeMode.observeAsState()
    
    @Composable
    fun ThemeModeOptionButton(
        text: String,
        value: UustSettings.ThemeMode
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        
        Surface(
            onClick = { UustSettings.themeMode.set(value) },
            interactionSource = interactionSource
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = text
                )

                RadioButton(
                    selected = theme == value,
                    onClick = { UustSettings.themeMode.set(value) },
                    interactionSource = interactionSource
                )
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Text("Тема")
        
        ThemeModeOptionButton(
            text = "Как в системе",
            value = UustSettings.ThemeMode.SYSTEM
        )

        ThemeModeOptionButton(
            text = "Светлая",
            value = UustSettings.ThemeMode.LIGHT
        )

        ThemeModeOptionButton(
            text = "Темная",
            value = UustSettings.ThemeMode.DARK
        )
    }
}