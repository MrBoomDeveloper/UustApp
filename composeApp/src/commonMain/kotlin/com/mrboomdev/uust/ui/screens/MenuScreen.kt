package com.mrboomdev.uust.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.LocalBackStack
import com.mrboomdev.uust.resources.Res
import com.mrboomdev.uust.resources.logo_amaterasu
import com.mrboomdev.uust.resources.logo_profplus
import com.mrboomdev.uust.resources.logo_sks
import com.mrboomdev.uust.ui.components.ClubAds
import com.mrboomdev.uust.ui.navigation.Routes
import org.jetbrains.compose.resources.painterResource

@Composable
fun MenuScreen(
    contentPadding: PaddingValues
) {
    val uriHandler = LocalUriHandler.current
    val backStack = LocalBackStack.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            text = "Весело проведи время"
        )

        ClubAds(
            onClick = {
                backStack += Routes.Clubs
            }
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            text = "Выгодно студенту"
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            onClick = {
                uriHandler.openUri("https://sksrf.ru/sksrfbot")
            }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    modifier = Modifier
                        .height(32.dp),
                    painter = painterResource(Res.drawable.logo_sks),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    text = "СКС Профсоюза"
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            onClick = {
                uriHandler.openUri("https://www.rustore.ru/catalog/app/com.dlilb.profplus")
            }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                        .size(48.dp),
                    painter = painterResource(Res.drawable.logo_profplus),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    text = "ПрофПлюс"
                )
            }
        }
    }
}