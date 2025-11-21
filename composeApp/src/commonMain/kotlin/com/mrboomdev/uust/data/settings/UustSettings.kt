package com.mrboomdev.uust.data.settings

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings

internal val settings = Settings() as ObservableSettings

object UustSettings {
    val scheduleInRow = BooleanSetting("scheduleInRow", false)
    val outlinedSchedule = BooleanSetting("outlinedSchedule", true)
    val themeMode = EnumSetting("themeMode", ThemeMode.SYSTEM)
    val hideClubAds = BooleanSetting("hideClubAds", false)
    
    enum class ThemeMode {
        SYSTEM, LIGHT, DARK
    }
}