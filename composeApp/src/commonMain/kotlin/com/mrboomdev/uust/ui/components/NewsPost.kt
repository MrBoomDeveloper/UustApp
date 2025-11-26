package com.mrboomdev.uust.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mrboomdev.uust.ui.UustTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import uust.composeapp.generated.resources.Res
import uust.composeapp.generated.resources.golos_text_medium
import uust.composeapp.generated.resources.golos_text_regular

@Composable
fun NewsPost(
    modifier: Modifier = Modifier,
    category: String,
    categoryColor: Color = Color(0xff723bff),
    categoryIcon: DrawableResource? = null,
    banner: String,
    title: String,
    date: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    model = banner,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                ) 
                
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(categoryColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val onCategoryColor = contentColorFor(categoryColor)
                    
                    categoryIcon?.also {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(categoryIcon),
                            tint = onCategoryColor,
                            contentDescription = null
                        )
                    }
                    
                    Text(
                        fontFamily = UustTheme.fonts.golos,
                        style = MaterialTheme.typography.bodySmall,
                        color = onCategoryColor,
                        text = category
                    )
                }
            }

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
}