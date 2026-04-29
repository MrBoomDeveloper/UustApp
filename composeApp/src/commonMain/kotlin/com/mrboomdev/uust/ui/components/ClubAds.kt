package com.mrboomdev.uust.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrboomdev.uust.resources.Res
import com.mrboomdev.uust.resources.clubs
import com.mrboomdev.uust.ui.UustTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun ClubAds(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
//    onClose: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.primaryContainer,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .height(112.dp)
        ) {
            Row {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clipToBounds()
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(10f)
                            .scale(1.2f),
                        painter = painterResource(Res.drawable.clubs),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = UustTheme.fonts.golos,
                        text = "Вступай в клубы!"
                    )

                    Text(
                        fontSize = 15.sp,
                        fontFamily = UustTheme.fonts.golos,
                        overflow = TextOverflow.Ellipsis,
                        text = "Проводи свободное время занимаясь любимым делом"
                    )
                }
            }

//                    IconButton(
//                        onClick = {}
//                    ) {
//                        Icon(
//                            painter = painterResource(Res.drawable.clos)
//                        )
//                    }
        }
    }
}