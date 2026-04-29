package com.mrboomdev.uust.ui.widgets

import android.R.attr.text
import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import androidx.glance.color.colorProviders
import androidx.glance.material3.ColorProviders
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.mrboomdev.uust.ui.MainActivity
import com.mrboomdev.uust.ui.UustTheme
import kotlin.time.ExperimentalTime

class TimerWidget : GlanceAppWidgetReceiver() {
    @OptIn(ExperimentalTime::class)
    override val glanceAppWidget: GlanceAppWidget = Impl()

    class Impl : GlanceAppWidget() {
        override suspend fun provideGlance(
            context: Context,
            id: GlanceId
        ) {
            provideContent {
                GlanceTheme(
                    colors = ColorProviders(
                        light = UustTheme.lightColorScheme(),
                        dark = UustTheme.darkColorScheme()
                    )
                ) {
                    Scaffold(
                        modifier = GlanceModifier
                            .clickable(actionStartActivity<MainActivity>()),

                        backgroundColor = GlanceTheme.colors.surface,
                    ) {
                        Text(
                            text = "TODO: Implement timer widget"
                        )
                    }
                }
            }
        }
    }
}