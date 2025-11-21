package com.mrboomdev.uust.data.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.StorageSettings
import com.russhwolf.settings.observable.makeObservable

@OptIn(ExperimentalSettingsApi::class)
internal actual val settings: ObservableSettings = 
    StorageSettings().makeObservable()