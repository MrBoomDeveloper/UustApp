package com.mrboomdev.uust.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.mrboomdev.uust.data.api.UustTimeSchedule
import com.mrboomdev.uust.ui.screens.*
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes: NavKey {
    @Serializable
    data object Welcome: Routes {
        @Composable
        override fun Content(
            contentPadding: PaddingValues
        ) = WelcomeScreen(contentPadding = contentPadding)
    }

    data object Settings: Routes {
        override val title = "Настройки"
        @Composable
        override fun Content(contentPadding: PaddingValues) {
            SettingsScreen(contentPadding)
        }
    }

    @Serializable
    data object Home: Routes {
        @Composable
        override fun Content(
            contentPadding: PaddingValues
        ) = HomeScreen(contentPadding = contentPadding)
    }

    @Serializable
    data object Navigation: Routes {
        @Composable
        override fun Content(
            contentPadding: PaddingValues
        ) = MapScreen(contentPadding)
    }

    @Serializable
    data object Help: Routes {
        @Composable
        override fun Content(
            contentPadding: PaddingValues
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                text = "This screen isn't done yet!"
            )
        }
    }

    @Serializable
    data object Calendar: Routes {
        override val title = "Расписание"
        @Composable
        override fun Content(
            contentPadding: PaddingValues
        ) = CalendarScreen(contentPadding = contentPadding)
    }
    
    @Serializable
    data class Crash(val error: String): Routes {
        override val title = "Произошла ошибка"
        @Composable
        override fun Content(
            contentPadding: PaddingValues
        ) = CrashScreen(contentPadding, error)
    }

    @Serializable
    data class Schedule(
        val schedule: UustTimeSchedule
    ) : Routes {
        override val title = "Занятие"

        @Composable
        override fun Content(
            contentPadding: PaddingValues
        ) = ScheduleScreen(contentPadding, schedule)
    }

    @Composable
    fun Content(contentPadding: PaddingValues)

    val title: String get() = "Мы - УУНиТ"
}