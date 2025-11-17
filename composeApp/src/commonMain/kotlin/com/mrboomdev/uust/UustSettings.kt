package com.mrboomdev.uust

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getBooleanStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

private val settings = Settings() as ObservableSettings

object UustSettings {
    val scheduleInRow = BooleanSetting("scheduleInRow", false)
    val outlinedSchedule = BooleanSetting("outlinedSchedule", true)
}

sealed interface Setting<T> {
    val value: T
    fun set(value: T)
    fun observe(coroutineScope: CoroutineScope): StateFlow<T>
}

@Composable
fun <T> Setting<T>.observeAsState() = 
    observe(rememberCoroutineScope()).collectAsState()

class BooleanSetting(
    private val key: String,
    private val initialValue: Boolean
): Setting<Boolean> {
    override val value get() = settings.getBoolean(key, initialValue)
    override fun set(value: Boolean) = settings.putBoolean(key, value)
    
    @OptIn(ExperimentalSettingsApi::class)
    override fun observe(coroutineScope: CoroutineScope) = 
        settings.getBooleanStateFlow(coroutineScope, key, initialValue)
    
    fun toggle() = set(!value)
}