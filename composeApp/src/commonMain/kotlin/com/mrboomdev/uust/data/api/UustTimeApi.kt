package com.mrboomdev.uust.data.api

import com.mrboomdev.uust.Platform
import com.mrboomdev.uust.Uust
import com.mrboomdev.uust.platform
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object UustTimeApi {
    suspend fun fetchSchedule(
        // TODO: Add params
    ): List<UustTimeSchedule> = Uust.httpClient.get(
        if(Uust.platform == Platform.WEB) {
            // Cors policy is blocking api access, so we do use proxy
            "http://awery.mrboomdev.ru/uustSchedule"
        } else "https://dev.uust-time.ru/api/v/852972/schedule/0/9573/semester/241?site=schedule"
    ) {
        header("origin", "https://schedule.uust.ru")
    }.let { response ->
        Json.decodeFromString(response.bodyAsText().also { println(it) })
    }
}

@Serializable
data class UustTimeSchedule(
    val index: Int,
    val student_group_number_id: Int,
    val schedule_id: Int,
    val schedule_weeks: List<String>,
    val schedule_subject_title: String,
    val schedule_weekday_id: Int,
    val schedule_time_new_id: Int,
    val schedule_time_title: String,
    val building_short_title: String,
    val building_title: String,
    val room_id: Int,
    val room_title: String,
    val comment: String,
    val type: String,
    val teacher: String,
    val teacher_id: Int,
    val teacher_fullname: String,
    val room_title_short: String
)

//@OptIn(ExperimentalTime::class)
//fun UustTimeSchedule.toScheduleItems() {
//    val today = Clock.System.now().toLocalDateTime(
//        TimeZone.currentSystemDefault()
//    )
//    
//    return schedule_weeks.map { it.toInt() }.map { weekNumber ->
//        
//    }
//    
//    return ScheduleItem(
//        type = type,
//        number = schedule_time_new_id,
//        subject = schedule_subject_title,
//        start = TODO(),
//        end = TODO(),
//        week = TODO(),
//        note = comment.takeUnless { it.isBlank() },
//        teacher = teacher_fullname.takeUnless { it.isBlank() },
//        location = "${building_short_title}/${room_title}"
//    )
//}