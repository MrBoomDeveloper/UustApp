package com.mrboomdev.uust.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.UustTheme

@Composable
fun CrashScreen(
    contentPadding: PaddingValues,
    errorMessage: String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) { 
        item(
            key = "title",
            contentType = "title"
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp, 
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),

                fontFamily = UustTheme.fonts.golos,
                style = MaterialTheme.typography.titleLarge,
                text = "Произошла ошибка!"
            )
        }
        
        item(
            key = "message",
            contentType = "message"
        ) {
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),

                    fontFamily = UustTheme.fonts.golos,
                    style = MaterialTheme.typography.bodyLarge,
                    text = errorMessage
                )
            }
        }
    }
}