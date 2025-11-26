package com.mrboomdev.uust.ui.screens

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrboomdev.uust.LocalBackStack
import com.mrboomdev.uust.data.Holidays
import com.mrboomdev.uust.data.api.UustTimeApi
import com.mrboomdev.uust.data.api.UustTimeSchedule
import com.mrboomdev.uust.data.settings.UustSettings
import com.mrboomdev.uust.data.settings.observeAsState
import com.mrboomdev.uust.navigate
import com.mrboomdev.uust.ui.UustTheme
import com.mrboomdev.uust.ui.components.Holiday
import com.mrboomdev.uust.ui.components.ScheduleItemProgress
import com.mrboomdev.uust.ui.components.SchedulePreview
import com.mrboomdev.uust.ui.navigation.Routes
import com.mrboomdev.uust.utils.collectAsStateAndCache
import com.mrboomdev.uust.utils.exclude
import com.mrboomdev.uust.utils.ifThen
import com.mrboomdev.uust.utils.toLocalDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.format.char
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import uust.composeapp.generated.resources.*
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class CalendarViewModel: ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    
    private val _schedules = MutableStateFlow(emptyList<UustTimeSchedule>())
    val schedules = _schedules.asStateFlow()

    init {
        viewModelScope.launch {
            _schedules.emit(UustTimeApi.fetchSchedule())
            _isLoading.emit(false)
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

private const val MAX_MILLIS_OF_DAY = 86400L * 1000000000L - 1

@OptIn(ExperimentalTime::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel { CalendarViewModel() },
    contentPadding: PaddingValues
) {
    val density = LocalDensity.current
    val backStack = LocalBackStack.current
    val coroutineScope = rememberCoroutineScope()
    var currentTime by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }
    var toolbarHeight by remember { mutableStateOf(0.dp) }
    val useOutlinedSchedule by UustSettings.outlinedSchedule.observeAsState()

    fun getInitialDay(): Int {
        val daysUntil = firstDateOfEdu.daysUntil(currentTime.toLocalDate())
        return (daysUntil / 7) * 6 + (daysUntil % 7)
    }
    
    val pagerState = rememberPagerState(
        initialPage = getInitialDay(),
        pageCount = { (365.toFloat() / 7 * 6).toInt() }
    )
    
    val currentEduDay = remember(currentTime) { getInitialDay() }
    val selectedEduWeek = pagerState.currentPage / 6
    val selectedDayOfWeek = pagerState.currentPage % 6
    val isSunday = currentTime.dayOfWeek == DayOfWeek.SUNDAY

    val buttonColors = IconButtonDefaults.filledIconButtonColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.primary
    )
    
    LaunchedEffect(Unit) {
        while(true) {
            currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            delay(10_000)
        }
    }
    
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            PrimaryTabRow(
                modifier = Modifier
                    .padding(contentPadding.exclude(bottom = true)),
                
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

                val pagerDate = remember(educationDay) {
                    LocalDate.fromEpochDays(firstDateOfEdu.toEpochDays() + educationDay + (educationDay / 6))
                }

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
                        .ifThen(schedules.isNotEmpty()) {
                            verticalScroll(rememberScrollState())
                        }.padding(contentPadding.exclude(top = true))
                        .padding(bottom = toolbarHeight)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),

                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = UustTheme.fonts.golos,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary,

                        text = buildString {
                            append(pagerDate.day)
                            append(" ")

                            append(when(pagerDate.month) {
                                Month.JANUARY -> "Января"
                                Month.FEBRUARY -> "Февраля"
                                Month.MARCH -> "Марта"
                                Month.APRIL -> "Апреля"
                                Month.MAY -> "Мая"
                                Month.JUNE -> "Июня"
                                Month.JULY -> "Июля"
                                Month.AUGUST -> "Августа"
                                Month.SEPTEMBER -> "Сентября"
                                Month.OCTOBER -> "Октября"
                                Month.NOVEMBER -> "Ноября"
                                Month.DECEMBER -> "Декабря"
                            })
                            
                            when {
                                educationDay == currentEduDay && !isSunday -> append(" (Сегодня)")
                                (educationDay == currentEduDay + 1 && currentTime.dayOfWeek < DayOfWeek.SATURDAY) || (educationDay == currentEduDay && isSunday) -> append(" (Завтра)")
                                (educationDay == currentEduDay + 2 && currentTime.dayOfWeek < DayOfWeek.FRIDAY) || (educationDay == currentEduDay + 1 && isSunday) || (educationDay == currentEduDay + 1 && currentTime.dayOfWeek == DayOfWeek.SATURDAY) -> append(" (Послезавтра)")
                                educationDay == currentEduDay - 1 && currentTime.dayOfWeek != DayOfWeek.MONDAY -> append(" (Вчера)")
                                (educationDay == currentEduDay - 2 && currentTime.dayOfWeek > DayOfWeek.TUESDAY) || (educationDay == currentEduDay - 1 && currentTime.dayOfWeek == DayOfWeek.MONDAY) -> append(" (Позавчера)")
                            }
                        }
                    )

                    if(schedules.isEmpty()) {
                        remember(pagerDate) {
                            Holidays.all.firstOrNull { holidayDate ->
                                holidayDate.first == pagerDate.month && holidayDate.second == pagerDate.day
                            }
                        }?.also { holiday ->
                            Holiday(
                                modifier = Modifier
                                    .padding(32.dp)
                                    .fillMaxWidth()
                                    .weight(1f),
                                name = "Праздник!" // TODO: Replace with an actual holiday name
                            )

                            return@HorizontalPager
                        }

                        if(viewModel.isLoading.collectAsState().value) {
                            LoadingIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(Alignment.Center)
                            )

                            return@Column
                        }

                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center),
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = UustTheme.fonts.golos,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                            text = "Занятия отсутствуют"
                        )

                        return@HorizontalPager
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        schedules.forEach { (schedule, scheduleInfo) ->
                            val daysDiffInMinutes = (educationDay - currentEduDay + if(isSunday) 1 else 0) * 24 * 60
                            val startMinute = scheduleInfo.timeFrom.hour * 60 + scheduleInfo.timeFrom.minute + daysDiffInMinutes
                            val endMinute = scheduleInfo.timeTo.hour * 60 + scheduleInfo.timeTo.minute + daysDiffInMinutes
                            val currentMinute = currentTime.hour * 60 + currentTime.minute

                            val progress = when {
                                educationDay > currentEduDay -> ScheduleItemProgress.SOON
                                educationDay < currentEduDay -> ScheduleItemProgress.COMPLETED
                                currentMinute > endMinute -> ScheduleItemProgress.COMPLETED
                                schedule == currentSchedule?.first && !isSunday -> ScheduleItemProgress.NOW
                                else -> ScheduleItemProgress.SOON
                            }

                            SchedulePreview(
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
                                progress = progress,

                                footer = ((startMinute - currentMinute) * 60L * 1000L).takeIf {
                                    it > 0L && progress != ScheduleItemProgress.COMPLETED
                                }?.let { timeBeforeBeginning ->
                                    buildString {
                                        append("До начала: ")

                                        if(timeBeforeBeginning.toInt() * 1_000_000L !in 1..<MAX_MILLIS_OF_DAY) {
                                            append(
                                                HumanReadable.timeAgo(
                                                    Instant.fromEpochSeconds(startMinute.toLong() * 60),
                                                    Instant.fromEpochSeconds(currentMinute.toLong() * 60))
                                            )
                                        } else {
                                            append(LocalTime.fromMillisecondOfDay(
                                                timeBeforeBeginning.toInt()
                                            ).format(LocalTime.Format {
                                                hour()
                                                char(':')
                                                minute()
                                            }))
                                        }
                                    }
                                } ?: ((endMinute - currentMinute) * 60L * 1000L).takeIf {
                                    it > 0L && it < 80 * 60 * 1000 && progress != ScheduleItemProgress.COMPLETED
                                }?.let { timeBeforeEnd ->
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

                                onClick = {
                                    backStack.navigate(Routes.Schedule(schedule))
                                }
                            )
                        }
                    }
                }
            }
        }
        
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = animateDpAsState(contentPadding.calculateBottomPadding() + 16.dp).value)
                .onGloballyPositioned { coordinates ->
                    toolbarHeight = with(density) { coordinates.size.height.toDp() + 16.dp }
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(64.dp))
                    .background(buttonColors.containerColor)
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    modifier = Modifier.size(48.dp),
                    colors = buttonColors,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(max(0, pagerState.currentPage - 6))
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),

                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = null
                    )
                }

                Text(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily(Font(Res.font.golos_text_bold)),
                    text = "Неделя ${selectedEduWeek + 1}"
                )

                FilledIconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .scale(scaleX = -1f, scaleY = 1f),

                    colors = buttonColors,

                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(min(pagerState.pageCount, pagerState.currentPage + 6))
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),

                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = null
                    )
                }
            }
            
            FilledIconButton(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(46.dp),
                
                shape = RoundedCornerShape(12.dp),
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
                        .padding(10.dp),

                    painter = painterResource(Res.drawable.ic_undo),
                    contentDescription = null
                )
            }
        }
    }
}