package com.mrboomdev.uust.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import uust.composeapp.generated.resources.*

@Composable
fun WarningScreen(
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.Red)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeContent)
                .padding(32.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(Res.font.golos_text_regular)),
            color = Color.White,
            text = "Уфимский Университет Науки и Технологий"
        )
        
        Spacer(Modifier.weight(1f))

        Icon(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(128.dp),
            painter = painterResource(Res.drawable.ic_warning_filled),
            contentDescription = null
        )
        
        Text(
            fontSize = 64.sp,
            fontFamily = FontFamily(Font(Res.font.golos_text_bold)),
            color = Color.White,
            text = "ТРЕВОГА"
        )

        Text(
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp),
            fontFamily = FontFamily(Font(Res.font.golos_text_regular)),
            color = Color.White,
            text = "БЕСПИЛОТНАЯ ОПАСНОСТЬ"
        )

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(bottom = 64.dp, top = 64.dp),
            
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
            
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            
            onClick = {
                onDismissRequest()
            }
        ) {
            Text(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(Res.font.golos_text_medium)),
                text = "Я В БЕЗОПАСНОСТИ"
            )
        }
    }
}