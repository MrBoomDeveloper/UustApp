package com.mrboomdev.uust.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrboomdev.uust.UustSettings
import com.mrboomdev.uust.components.ScheduleItem
import com.mrboomdev.uust.components.ScheduleItemProgress
import com.mrboomdev.uust.data.api.UustTimeApi
import com.mrboomdev.uust.data.api.UustTimeSchedule
import com.mrboomdev.uust.observeAsState
import com.mrboomdev.uust.utils.collectAsStateAndCache
import com.mrboomdev.uust.utils.toLocalDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import uust.composeapp.generated.resources.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CalendarViewModel: ViewModel() {
    private val _schedules = MutableStateFlow(emptyList<UustTimeSchedule>())
    val schedules = _schedules.asStateFlow()

    init {
        viewModelScope.launch {
            _schedules.emit(UustTimeApi.fetchSchedule())
        }
    }
}

@OptIn(ExperimentalTime::class)
private val initDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

@OptIn(ExperimentalTime::class)
private val firstDateOfEdu = if(initDate.month >= Month.SEPTEMBER) {
    LocalDate(initDate.year, Month.SEPTEMBER, 1)
} else LocalDate(initDate.year - 1, Month.SEPTEMBER, 1)

private val lastDateOfEdu = if(initDate.month >= Month.SEPTEMBER) {
    LocalDate(initDate.year + 1, Month.SEPTEMBER, 1)
} else LocalDate(initDate.year, Month.SEPTEMBER, 1)

@OptIn(ExperimentalTime::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel { CalendarViewModel() },
    contentPadding: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()
    var currentTime by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }
    val useOutlinedSchedule by UustSettings.outlinedSchedule.observeAsState()
    
    fun getInitialDay(): Int {
        return firstDateOfEdu.daysUntil(currentTime.toLocalDate()).toFloat().div(7f).times(6f).toInt() + 1
    }
    
    val pagerState = rememberPagerState(
        initialPage = getInitialDay(),
        pageCount = { (365.toFloat() / 7 * 6).toInt() }
    )
    
    val currentEduDay = remember(currentTime) { getInitialDay() }
    val selectedEduWeek = remember(pagerState.currentPage) { pagerState.currentPage / 6 }
    val selectedDayOfWeek = remember(pagerState.currentPage) { pagerState.currentPage % 6 }
    
    LaunchedEffect(Unit) {
        while(true) {
            currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            delay(10_000)
        }
    }
    
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val buttonColors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )

            FilledIconButton(
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = buttonColors,
                onClick = {}
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null
                )
            }
                
            Text(
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily(Font(Res.font.golos_text_bold)),
                text = "Неделя ${selectedEduWeek + 1}"
            )

            FilledIconButton(
                modifier = Modifier
                    .size(32.dp)
                    .scale(scaleX = -1f, scaleY = 1f),
                
                shape = RoundedCornerShape(8.dp),
                colors = buttonColors,
                onClick = {}
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),

                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null
                )
            }

            Spacer(Modifier.weight(1f))

            FilledIconButton(
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = buttonColors,
                onClick = {
                    coroutineScope.launch { 
                        pagerState.animateScrollToPage(getInitialDay())
                    }
                }
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),

                    painter = painterResource(Res.drawable.ic_undo),
                    contentDescription = null
                )
            }
        }
        
        PrimaryTabRow(
            selectedTabIndex = selectedDayOfWeek,
            divider = {
                HorizontalDivider(Modifier.alpha(.25f))
            }
        ) {
            for(i in 0..5) {
                Tab(
                    selected = i == selectedDayOfWeek,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + (i - selectedDayOfWeek))
                        }
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontFamily = FontFamily(Font(Res.font.golos_text_medium)),
                        text = when(i) {
                            0 -> "ПН"
                            1 -> "ВТ"
                            2 -> "СР"
                            3 -> "ЧТ"
                            4 -> "ПТ"
                            5 -> "СБ"
                            else -> i.toString()
                        }
                    )
                }
            }
        }
        
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { educationDay ->
            val pagerEduWeek = remember(educationDay) { educationDay / 6 + 1 }
            val pagerDayOfWeek = remember(educationDay) { educationDay % 6 }
            
            val schedules by viewModel.schedules.map { schedules ->
                schedules.filter { schedule ->
                    schedule.schedule_time_title.isNotBlank()
                            && schedule.schedule_weekday_id == pagerDayOfWeek + 1
                            && schedule.schedule_weeks.any { it.toInt() == pagerEduWeek }
                }.map { schedule ->
                    schedule to ScheduleInfo(
                        timeFrom = LocalTime.parse(schedule.schedule_time_title.substringBefore("-")),
                        timeTo = LocalTime.parse(schedule.schedule_time_title.substringAfter("-"))
                    )
                }
            }.collectAsStateAndCache(emptyList())

            val currentSchedule = remember(currentTime, schedules) {
                val predicate: (Pair<UustTimeSchedule, ScheduleInfo>) -> Boolean = { (schedule, scheduleInfo) ->
                    val startMinute = scheduleInfo.timeTo.hour * 60 + scheduleInfo.timeTo.minute
                    val currentMinute = currentTime.hour * 60 + currentTime.minute
                    startMinute >= currentMinute && educationDay == currentEduDay
                }

                schedules.firstOrNull(predicate)?.first?.let { schedule ->
                    schedule to schedules.indexOfFirst(predicate)
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    schedules.forEach { (schedule, scheduleInfo) ->
                        val startMinute = scheduleInfo.timeFrom.hour * 60 + scheduleInfo.timeFrom.minute
                        val endMinute = scheduleInfo.timeTo.hour * 60 + scheduleInfo.timeTo.minute
                        val currentMinute = currentTime.hour * 60 + currentTime.minute

                        ScheduleItem(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth(),
                            outlined = useOutlinedSchedule,
                            type = schedule.type,
                            index = schedule.schedule_time_new_id,
                            time = schedule.schedule_time_title,
                            name = schedule.schedule_subject_title,
                            note = schedule.comment.takeIf { it.isNotBlank() },
                            location = "${schedule.building_short_title} ${schedule.room_title}",
                            teacher = schedule.teacher_fullname.takeUnless { it.isBlank() },

                            footer = ((startMinute - currentMinute) * 60L * 1000L).takeIf { it > 0L }?.let { timeBeforeBeginning ->
                                buildString {
                                    append("До начала: ")

                                    append(LocalTime.fromMillisecondOfDay(
                                        timeBeforeBeginning.toInt()
                                    ).format(LocalTime.Format {
                                        hour()
                                        char(':')
                                        minute()
                                    }))
                                }
                            } ?: ((endMinute - currentMinute) * 60L * 1000L).takeIf { it > 0L && it < 80 * 60 * 1000 }?.let { timeBeforeEnd ->
                                buildString {
                                    append("До конца: ")

                                    append(LocalTime.fromMillisecondOfDay(
                                        timeBeforeEnd.toInt()
                                    ).format(LocalTime.Format {
                                        hour()
                                        char(':')
                                        minute()
                                    }))
                                }
                            },

                            progress = when {
                                currentMinute > endMinute -> ScheduleItemProgress.COMPLETED
                                schedule == currentSchedule?.first -> ScheduleItemProgress.NOW
                                else -> ScheduleItemProgress.SOON
                            },

                            onClick = {

                            }
                        )
                    }
                }
            }
        }
    }
}