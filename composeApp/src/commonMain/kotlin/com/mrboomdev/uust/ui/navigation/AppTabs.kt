package com.mrboomdev.uust.ui.navigation

import org.jetbrains.compose.resources.DrawableResource
import uust.composeapp.generated.resources.*

enum class AppTabs(
    val title: String,
    val icon: DrawableResource,
    val activeIcon: DrawableResource = icon,
    val defaultRoute: Routes
) {
    HOME(
        title = "Главная",
        icon = Res.drawable.ic_home_outlined,
        activeIcon = Res.drawable.ic_home_filled,
        defaultRoute = Routes.Home
    ),

    NAVIGATION(
        title = "Навигация",
        icon = Res.drawable.ic_explore_outlined,
        activeIcon = Res.drawable.ic_explore_filled,
        defaultRoute = Routes.Navigation
    ),

    INFO(
        title = "Инфо",
        icon = Res.drawable.ic_help_outlined,
        defaultRoute = Routes.Help
    )
}