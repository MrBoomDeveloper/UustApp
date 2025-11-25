package com.mrboomdev.uust.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.UustTheme
import com.mrboomdev.uust.data.settings.UustSettings
import com.mrboomdev.uust.data.settings.observeAsState

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
            color = Color.Transparent,
            onClick = { UustSettings.themeMode.set(value) },
            interactionSource = interactionSource
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    fontFamily = UustTheme.fonts.golos,
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
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            fontFamily = UustTheme.fonts.golos,
            text = "Учетная запись ИСУ"
        )
        
        Button(
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
            onClick = {
                
            }
        ) {
            Text("Вход пока не реализован")
        }
        
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            fontFamily = UustTheme.fonts.golos,
            text = "Тема"
        )
        
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