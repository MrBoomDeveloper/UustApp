package com.mrboomdev.uust.data.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.getBooleanStateFlow
import com.russhwolf.settings.coroutines.getStringStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries

sealed interface Setting<T> {
    val defaultValue: T

    fun set(value: T)

    fun get(defaultValue: T): T

    fun observe(
        coroutineScope: CoroutineScope,
        defaultValue: T
    ): StateFlow<T>
}

class BooleanSetting(
    private val key: String,
    override val defaultValue: Boolean
): Setting<Boolean> {
    override fun set(
        value: Boolean
    ) = settings.putBoolean(key, value)

    override fun get(
        defaultValue: Boolean
    ) = settings.getBoolean(key, defaultValue)

    @OptIn(ExperimentalSettingsApi::class)
    override fun observe(
        coroutineScope: CoroutineScope,
        defaultValue: Boolean
    ) = settings.getBooleanStateFlow(coroutineScope, key, defaultValue)

    fun toggle() = set(!value)
}

class StringSetting(
    private val key: String,
    override val defaultValue: String
): Setting<String> {
    override fun set(
        value: String
    ) = settings.putString(key, value)

    override fun get(
        defaultValue: String
    ) = settings.getString(key, defaultValue)

    @OptIn(ExperimentalSettingsApi::class)
    override fun observe(
        coroutineScope: CoroutineScope,
        defaultValue: String
    ) = settings.getStringStateFlow(coroutineScope, key, defaultValue)
}

class EnumSetting<T: Enum<T>>(
    private val key: String,
    override val defaultValue: T,
    private val entries: EnumEntries<T>
): Setting<T> {
    override fun set(
        value: T
    ) = settings.putString(key, value.name)

    override fun get(
        defaultValue: T
    ) = settings.getString(key, defaultValue.name).let {
        enumValue -> entries.first { enumEntry ->
            enumEntry.name == enumValue
        }
    }

    @OptIn(ExperimentalSettingsApi::class)
    override fun observe(
        coroutineScope: CoroutineScope,
        defaultValue: T
    ) = settings.getStringStateFlow(
        coroutineScope,
        key,
        defaultValue.name
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
    defaultValue: T
) = EnumSetting(
    key = key,
    defaultValue = defaultValue,
    entries = enumEntries<T>()
)