package com.mrboomdev.uust.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

fun LocalDate.getEducationWeek(): Int {
    if(month < Month.SEPTEMBER) {
        return Int.MAX_VALUE
    }
    
    val septemberFirst = LocalDate(year, Month.SEPTEMBER, 1)
    val daysBetween = this.toEpochDays() - septemberFirst.toEpochDays()
    return (daysBetween / 7).toInt() + 1
}

fun LocalDateTime.toLocalDate() = LocalDate(
    year = year,
    month = month,
    day = day
)