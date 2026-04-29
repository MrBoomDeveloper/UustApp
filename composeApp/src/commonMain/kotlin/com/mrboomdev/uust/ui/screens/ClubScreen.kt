package com.mrboomdev.uust.ui.screens

import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.resources.Res
import com.mrboomdev.uust.resources.ic_clock_outlined
import com.mrboomdev.uust.resources.ic_help_outlined
import com.mrboomdev.uust.resources.ic_map_outlined
import com.mrboomdev.uust.resources.logo_vk
import com.mrboomdev.uust.resources.wallpaper_amaterasu
import kotlinx.datetime.format.Padding
import org.jetbrains.compose.resources.painterResource

@Composable
fun ClubScreen(
    contentPadding: PaddingValues
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                alpha = .25f,
                painter = painterResource(Res.drawable.wallpaper_amaterasu),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                text = "Аматэрасу"
            )
        }

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_clock_outlined),
                contentDescription = null
            )

            Text(
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                text = "Когда"
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.W800,
            text = "18:30-21:00"
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.W600,
            text = "Каждый четверг"
        )

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_map_outlined),
                contentDescription = null
            )

            Text(
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                text = "Где"
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Карла Маркса 12/9\nКаждая встреча в разной аудитории, так что следите за новостями в VK!"
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            onClick = {
                uriHandler.openUri("https://vk.com/amaterasu_uust")
            }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(Res.drawable.logo_vk),
                    contentDescription = null
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = "ВКонтакте"
                    )

                    Text("https://vk.com/amaterasu_uust")
                }
            }
        }

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_help_outlined),
                contentDescription = null
            )

            Text(
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                text = "Род деятельности"
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Аниме клуб «Аматэрасу» - это объединение людей, которые увлекаются японской мультипликацией, мангой и всем, что с этим связано. В клубе вы сможете насладиться приятной атмосферой в компании единомышленников, обсудить собственные идеи аниме-мероприятий."
        )
    }
}