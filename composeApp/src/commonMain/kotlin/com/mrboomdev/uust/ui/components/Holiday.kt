package com.mrboomdev.uust.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.ui.UustTheme

@Composable
fun Holiday(
    modifier: Modifier = Modifier,
    name: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            fontFamily = UustTheme.fonts.golos,
            textAlign = TextAlign.Center,
            text = name
        )
        
        Text(
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = UustTheme.fonts.golos,
            textAlign = TextAlign.Center,
            text = "Никуда не надо идти, отдыхаем!"
        )
    }
}