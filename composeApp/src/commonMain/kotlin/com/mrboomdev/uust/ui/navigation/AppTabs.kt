package com.mrboomdev.uust.ui.navigation

import com.mrboomdev.uust.resources.Res
import com.mrboomdev.uust.resources.ic_explore_filled
import com.mrboomdev.uust.resources.ic_explore_outlined
import com.mrboomdev.uust.resources.ic_help_outlined
import com.mrboomdev.uust.resources.ic_home_filled
import com.mrboomdev.uust.resources.ic_home_outlined
import com.mrboomdev.uust.resources.ic_menu
import com.mrboomdev.uust.resources.ic_school_filled
import com.mrboomdev.uust.resources.ic_school_outlined
import org.jetbrains.compose.resources.DrawableResource

enum class AppTabs(
    val title: String,
    val icon: DrawableResource,
    val activeIcon: DrawableResource = icon,
    val defaultRoute: Routes
) {
    HOME(
        title = "Лента",
        icon = Res.drawable.ic_home_outlined,
        activeIcon = Res.drawable.ic_home_filled,
        defaultRoute = Routes.Home
    ),

    NAVIGATION(
        title = "Карта",
        icon = Res.drawable.ic_explore_outlined,
        activeIcon = Res.drawable.ic_explore_filled,
        defaultRoute = Routes.Navigation
    ),

    SDO(
        title = "СЭО",
        icon = Res.drawable.ic_school_outlined,
        activeIcon = Res.drawable.ic_school_filled,
        defaultRoute = Routes.Sdo
    ),

    INFO(
        title = "Меню",
        icon = Res.drawable.ic_menu,
        defaultRoute = Routes.Help
    )
}