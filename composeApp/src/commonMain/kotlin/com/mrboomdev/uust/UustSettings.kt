package com.mrboomdev.uust

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getBooleanStateFlow
import com.russhwolf.settings.coroutines.getStringStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries

private val settings = Settings() as ObservableSettings

object UustSettings {
    val scheduleInRow = BooleanSetting("scheduleInRow", false)
    val outlinedSchedule = BooleanSetting("outlinedSchedule", true)
    val themeMode = EnumSetting("themeMode", ThemeMode.SYSTEM)
    
    enum class ThemeMode {
        SYSTEM, LIGHT, DARK
    }
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

class StringSetting(
    private val key: String,
    private val initialValue: String
): Setting<String> {
    override val value get() = settings.getString(key, initialValue)
    override fun set(value: String) = settings.putString(key, value)

    @OptIn(ExperimentalSettingsApi::class)
    override fun observe(coroutineScope: CoroutineScope) =
        settings.getStringStateFlow(coroutineScope, key, initialValue)
}

class EnumSetting<T: Enum<T>>(
    private val key: String,
    private val initialValue: T,
    private val entries: EnumEntries<T>
): Setting<T> {
    override val value get() = settings.getString(key, initialValue.name).let { 
        value -> entries.first {
            it.name == value
        }
    }
    
    override fun set(value: T) = settings.putString(key, value.name)

    @OptIn(ExperimentalSettingsApi::class)
    override fun observe(coroutineScope: CoroutineScope) =
        settings.getStringStateFlow(
            coroutineScope, 
            key, 
            initialValue.name
        ).map { value ->
            entries.first { it.name == value }
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = value
        )
}

inline fun <reified T: Enum<T>> EnumSetting(
    key: String,
    initialValue: T
) = EnumSetting(
    key = key,
    initialValue = initialValue,
    entries = enumEntries<T>()
)