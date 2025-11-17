package com.mrboomdev.uust.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleItem(
    val type: String,
    val number: Int,
    val subject: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val week: Int,
    val note: String? = null,
    val teacher: String? = null,
    val location: String? = null
)