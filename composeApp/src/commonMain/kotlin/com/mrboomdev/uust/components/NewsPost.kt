package com.mrboomdev.uust.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.Font
import uust.composeapp.generated.resources.Res
import uust.composeapp.generated.resources.golos_text_medium
import uust.composeapp.generated.resources.golos_text_regular

@Composable
fun NewsPost(
    modifier: Modifier = Modifier,
    banner: String,
    title: String,
    date: String
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            model = banner,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily(Font(Res.font.golos_text_medium)),
                text = title
            )

            Text(
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily(Font(Res.font.golos_text_regular)),
                text = date
            )
        }
    }
}