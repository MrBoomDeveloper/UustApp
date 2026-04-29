package com.mrboomdev.uust.data

import com.mrboomdev.uust.resources.Res
import com.mrboomdev.uust.resources.ic_3d_outlined
import com.mrboomdev.uust.resources.ic_binary
import com.mrboomdev.uust.resources.ic_code
import com.mrboomdev.uust.resources.ic_football_outlined
import com.mrboomdev.uust.resources.ic_function
import com.mrboomdev.uust.resources.ic_history_edu_outlined
import com.mrboomdev.uust.resources.ic_robot_filled
import com.mrboomdev.uust.resources.ic_robot_outlined
import org.jetbrains.compose.resources.DrawableResource
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