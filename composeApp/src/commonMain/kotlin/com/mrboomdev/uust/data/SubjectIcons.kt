package com.mrboomdev.uust.data

import org.jetbrains.compose.resources.DrawableResource
import uust.composeapp.generated.resources.*

data class SubjectIcon(
    val icon: DrawableResource,
    val activeIcon: DrawableResource = icon,
    val iconScale: Float = 1f,
)

val SubjectIcons = mapOf(
    "История России" to SubjectIcon(
        icon = Res.drawable.ic_history_edu_outlined
    ),
    
    "Алгебра и геометрия" to SubjectIcon(
        icon = Res.drawable.ic_function
    ),
    
    "Физическая культура и спорт" to SubjectIcon(
        icon = Res.drawable.ic_football_outlined, 
        iconScale = .9f
    ),
    
    "Языки и методы программирования" to SubjectIcon(
        icon = Res.drawable.ic_code
    ),
    
    "Дискретная математика и математическая логика" to SubjectIcon(
        icon = Res.drawable.ic_binary
    ),
    
    "Основы 3D-моделирования и визуализации данных" to SubjectIcon(
        icon = Res.drawable.ic_3d_outlined
    ),
    
    "Основы современных цифровых технологий и искусственного интеллекта" to SubjectIcon(
        icon = Res.drawable.ic_robot_outlined,
        activeIcon = Res.drawable.ic_robot_filled
    )
)