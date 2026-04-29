package com.mrboomdev.uust.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.resources.Res
import com.mrboomdev.uust.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen(
    contentPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(contentPadding)
    ) {
        Image(
            modifier = Modifier.size(64.dp),
            painter = painterResource(Res.drawable.logo),
            contentDescription = null
        )
        
        Spacer(Modifier.weight(1f))
        
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        ) {
            Text("Я абитуриент")
        }
        
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {}
            ) {
                Text("Я преподаватель")
            }
            
            Button(
                modifier = Modifier.weight(1f),
                onClick = {}
            ) {
                Text("Я студент")
            }
        }
    }
}