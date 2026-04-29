package com.mrboomdev.uust.ui.widgets

import android.R.attr.text
import android.R.attr.type
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.material3.ColorProviders
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.window.core.layout.WindowSizeClass
import com.mrboomdev.uust.data.Holidays
import com.mrboomdev.uust.data.api.UustTimeApi
import com.mrboomdev.uust.ui.MainActivity
import com.mrboomdev.uust.ui.UustTheme
import com.mrboomdev.uust.ui.components.ScheduleItemProgress
import com.mrboomdev.uust.ui.components.scheduleItemTypes
import com.mrboomdev.uust.ui.screens.ScheduleInfo
import com.mrboomdev.uust.utils.firstAndIndexOrNull
import com.mrboomdev.uust.utils.getEducationWeek
import com.mrboomdev.uust.utils.toLocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ScheduleWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = Impl()

    class Impl : GlanceAppWidget() {
        @SuppressLint("RestrictedApi")
        @OptIn(ExperimentalTime::class)
        override suspend fun provideGlance(
            context: Context,
            id: GlanceId
        ) {
            val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val currentEducationWeek = currentTime.toLocalDate().getEducationWeek()
            val currentDayOfWeek = currentTime.dayOfWeek.ordinal + 1

            val schedules = withContext(Dispatchers.IO) {
                UustTimeApi.fetchSchedule().filter { schedule ->
                    schedule.schedule_weekday_id == currentDayOfWeek
                            && schedule.schedule_weeks.any { it.toInt() == currentEducationWeek }
                }.map { schedule ->
                    schedule to ScheduleInfo(
                        timeFrom = LocalTime.parse(schedule.schedule_time_title.substringBefore("-")),
                        timeTo = LocalTime.parse(schedule.schedule_time_title.substringAfter("-"))
                    )
                }
            }

            val currentSchedule = schedules.firstAndIndexOrNull { (_, scheduleInfo) ->
                val startMinute = scheduleInfo.timeTo.hour * 60 + scheduleInfo.timeTo.minute
                val currentMinute = currentTime.hour * 60 + currentTime.minute
                startMinute >= currentMinute
            }

            val isHoliday = currentTime.let { currentDate ->
                Holidays.all.any { (holidayMonth, holidayDay) ->
                    currentDate.month == holidayMonth && currentDate.day == holidayDay
                }
            }

            provideContent {
                val windowSize = currentWindowAdaptiveInfo().windowSizeClass

                GlanceTheme(
                    colors = ColorProviders(
                        light = UustTheme.lightColorScheme(),
                        dark = UustTheme.darkColorScheme()
                    )
                ) {
                    CompositionLocalProvider(
                        LocalConfiguration provides context.resources.configuration
                    ) {
                        Scaffold(
                            modifier = GlanceModifier
                                .clickable(actionStartActivity<MainActivity>()),

                            horizontalPadding = 0.dp,
                            backgroundColor = GlanceTheme.colors.surface,

                            titleBar = {
                                TitleBar(
                                    startIcon = ImageProvider(com.mrboomdev.uust.R.drawable.logo),

                                    title = when(currentTime.dayOfWeek) {
                                        DayOfWeek.MONDAY -> "ПОНЕДЕЛЬНИК"
                                        DayOfWeek.TUESDAY -> "ВТОРНИК"
                                        DayOfWeek.WEDNESDAY -> "СРЕДА"
                                        DayOfWeek.THURSDAY -> "ЧЕТВЕРГ"
                                        DayOfWeek.FRIDAY -> "ПЯТНИЦА"
                                        DayOfWeek.SATURDAY -> "СУББОТА"
                                        DayOfWeek.SUNDAY -> "ВОСКРЕСЕНИЕ"
                                    } + " - ${currentTime.toLocalDate().getEducationWeek()} НЕДЕЛЯ"
                                )
                            }
                        ) {
                            LazyColumn {
                                items(
                                    items = schedules,
                                    itemId = { it.first.index.toLong() }
                                ) { (schedule, scheduleInfo) ->
                                    val startMinute = scheduleInfo.timeFrom.hour * 60 + scheduleInfo.timeFrom.minute
                                    val endMinute = scheduleInfo.timeTo.hour * 60 + scheduleInfo.timeTo.minute
                                    val currentMinute = currentTime.hour * 60 + currentTime.minute

                                    val untilBeginning =
                                        ((startMinute - currentMinute) * 60L * 1000L).takeIf { it > 0L }
                                    val untilEnding =
                                        ((endMinute - currentMinute) * 60L * 1000L).takeIf { it > 0L && it < 80 * 60 * 1000 }

                                    val progress = when {
                                        currentMinute > endMinute -> ScheduleItemProgress.COMPLETED
                                        schedule == currentSchedule?.first?.first -> ScheduleItemProgress.NOW
                                        else -> ScheduleItemProgress.SOON
                                    }

                                    val foundType = scheduleItemTypes[schedule.type]

                                    val onColor = when(progress) {
                                        ScheduleItemProgress.SOON,
                                        ScheduleItemProgress.COMPLETED -> GlanceTheme.colors.onSurface

                                        ScheduleItemProgress.NOW -> ColorProvider(
                                            contentColorFor(
                                                GlanceTheme.colors.onSurface.getColor(context)
                                            )
                                        )
                                    }

                                    Column(
                                        modifier = GlanceModifier
                                            .clickable(actionStartActivity<MainActivity>())
                                            .background(
                                                when(progress) {
                                                ScheduleItemProgress.SOON,
                                                ScheduleItemProgress.COMPLETED -> GlanceTheme.colors.surface

                                                ScheduleItemProgress.NOW -> foundType?.getColor()?.let {
                                                    ColorProvider(it)
                                                } ?: GlanceTheme.colors.surface
                                            }).padding(12.dp)
                                    ) {
                                        Text(
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = onColor
                                            ),

                                            text = buildString {
                                                append(schedule.schedule_time_new_id)
                                                append(". ")

                                                if(windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
                                                    append(schedule.type)
                                                    append(" ")
                                                }

                                                append(schedule.schedule_time_title)
                                            }
                                        )

                                        Text(
                                            modifier = GlanceModifier
                                                .padding(vertical = 4.dp),

                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = onColor
                                            ),

                                            text = schedule.schedule_subject_title
                                        )

                                        schedule.teacher_fullname.takeUnless { it.isBlank() }?.also { teacher ->
                                            Text(
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    color = onColor
                                                ),

                                                text = teacher
                                            )
                                        }

                                        Text(
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                color = onColor
                                            ),

                                            text = "${schedule.building_short_title}/${schedule.room_title}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}