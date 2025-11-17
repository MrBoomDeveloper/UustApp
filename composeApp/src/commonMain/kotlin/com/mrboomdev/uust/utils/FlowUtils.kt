package com.mrboomdev.uust.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.retain.retain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@OptIn(ExperimentalAtomicApi::class)
@Composable
fun <T> Flow<T>.collectAsStateAndCache(
    initialValue: T,
    context: CoroutineContext = EmptyCoroutineContext
): State<T> {
    val cache = retain { AtomicReference(initialValue) }
    return map { item -> item.also { cache.store(it) } }.collectAsState(cache.load(), context)
}