package com.mrboomdev.uust.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.Font
import uust.composeapp.generated.resources.Res
import uust.composeapp.generated.resources.golos_text_bold
import uust.composeapp.generated.resources.golos_text_regular

@Composable
fun LunchItem(
    image: String,
    imageScale: Float = 1f,
    name: String,
    price: String,
) {
    Column(
        modifier = Modifier.width(112.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .scale(imageScale),
            
            model = image,
            contentDescription = null
        )
        
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily(Font(Res.font.golos_text_bold)),
            text = name
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily(Font(Res.font.golos_text_regular)),
            text = price
        )
    }
}