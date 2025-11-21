package com.mrboomdev.uust.data.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

fun <T> Setting<T>.observe(
    coroutineScope: CoroutineScope
) = observe(coroutineScope, defaultValue)

val <T> Setting<T>.value
    get() = get(defaultValue)

@Composable
fun <T> Setting<T>.observeAsState(
    defaultValue: T = this.defaultValue
) = observe(
    coroutineScope = rememberCoroutineScope(), 
    defaultValue = defaultValue
).collectAsState()