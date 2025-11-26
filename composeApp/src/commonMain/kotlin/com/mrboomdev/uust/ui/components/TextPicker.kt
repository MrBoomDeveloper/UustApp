package com.mrboomdev.uust.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.ui.isDarkTheme

@Composable
fun TextPickerItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        modifier = Modifier.widthIn(min = 175.dp),
        onClick = onClick,
        text = text,

        contentPadding = PaddingValues(
            vertical = 8.dp, horizontal = 24.dp
        )
    )
}

@Composable
fun OutlinedTextPicker(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 1,
    isExpanded: Boolean,
    onExpand: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    Box {
        OutlinedTextField(
            modifier = modifier.clickable {
                onExpand(true)
            },

            value = text,
            maxLines = maxLines,
            onValueChange = {},
            readOnly = true,
            enabled = false,

            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpand(false) },
            shape = RoundedCornerShape(32.dp),

            containerColor = if(isDarkTheme()) {
                Color(0xFF2B252F)
            } else MaterialTheme.colorScheme.surfaceContainer
        ) {
            content()
        }
    }
}