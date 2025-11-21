package com.mrboomdev.uust.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.UustTheme

@Composable
fun Holiday(
    modifier: Modifier = Modifier,
    name: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            fontFamily = UustTheme.fonts.golos,
            textAlign = TextAlign.Center,
            text = name
        )
        
        Text(
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = UustTheme.fonts.golos,
            textAlign = TextAlign.Center,
            text = "Никуда не надо идти, отдыхаем!"
        )
    }
}