package com.mrboomdev.uust.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mrboomdev.uust.LocalBackStack
import com.mrboomdev.uust.data.api.UustTimeSchedule

@Composable
fun ScheduleScreen(
    contentPadding: PaddingValues,
    schedule: UustTimeSchedule
) {
    val backStack = LocalBackStack.current
    // TODO: Implement this screen

    LaunchedEffect(Unit) {
        backStack.removeLastOrNull()
    }

    Text("Screen not done yet!")
}