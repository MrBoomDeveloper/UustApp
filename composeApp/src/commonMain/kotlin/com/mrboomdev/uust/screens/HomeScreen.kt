package com.mrboomdev.uust.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrboomdev.uust.LocalBackStack
import com.mrboomdev.uust.UustTheme
import com.mrboomdev.uust.components.*
import com.mrboomdev.uust.data.Holidays
import com.mrboomdev.uust.data.api.UustTimeApi
import com.mrboomdev.uust.data.api.UustTimeSchedule
import com.mrboomdev.uust.data.settings.UustSettings
import com.mrboomdev.uust.data.settings.observeAsState
import com.mrboomdev.uust.isDarkTheme
import com.mrboomdev.uust.navigation.Routes
import com.mrboomdev.uust.utils.collectAsStateAndCache
import com.mrboomdev.uust.utils.firstAndIndexOrNull
import com.mrboomdev.uust.utils.getEducationWeek
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
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class ScheduleInfo(
    val timeFrom: LocalTime,
    val timeTo: LocalTime
)

class HomeViewModel: ViewModel() {
    private val _schedules = MutableStateFlow(emptyList<UustTimeSchedule>())
    val schedules = _schedules.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    
    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()
    
    init {
        viewModelScope.launch {
            _isLoading.emit(true)
            
            try {
                _schedules.emit(UustTimeApi.fetchSchedule())
                _isError.emit(false)
            } catch(e: Throwable) {
                e.printStackTrace()
                _isError.emit(true)
            }
            
            _isLoading.emit(false)
        }
    }
}

@OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    viewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    val backStack = LocalBackStack.current
    val coroutineScope = rememberCoroutineScope()
    val isLoadingSchedule by viewModel.isLoading.collectAsState()
    val isScheduleLoadFailed by viewModel.isError.collectAsState()
    var currentTime by remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }
    val currentEducationWeek = remember(currentTime) { currentTime.toLocalDate().getEducationWeek() }
    val currentDayOfWeek = remember(currentTime) { currentTime.dayOfWeek.ordinal + 1 }

    val schedules by viewModel.schedules.map { schedules ->
        schedules.filter { schedule ->
            schedule.schedule_weekday_id == currentDayOfWeek
                    && schedule.schedule_weeks.any { it.toInt() == currentEducationWeek }
        }.map { schedule ->
            schedule to ScheduleInfo(
                timeFrom = LocalTime.parse(schedule.schedule_time_title.substringBefore("-")),
                timeTo = LocalTime.parse(schedule.schedule_time_title.substringAfter("-"))
            )
        }
    }.collectAsStateAndCache(emptyList())

    val currentSchedule = remember(currentTime, schedules) {
        schedules.firstAndIndexOrNull { (_, scheduleInfo) ->
            val startMinute = scheduleInfo.timeTo.hour * 60 + scheduleInfo.timeTo.minute
            val currentMinute = currentTime.hour * 60 + currentTime.minute
            startMinute >= currentMinute
        }
    }

    val useOutlinedSchedule by UustSettings.outlinedSchedule.observeAsState(
        // It looks ugly in the dark theme
        defaultValue = !isDarkTheme()
    )
    
    val showScheduleInRow by UustSettings.scheduleInRow.observeAsState()
    val hideClubAds by UustSettings.hideClubAds.observeAsState()
    val scheduleListState = rememberLazyListState()
    
    val isHoliday = remember(currentTime) {
        currentTime.let { currentDate ->
            Holidays.all.any { (holidayMonth, holidayDay) ->
                currentDate.month == holidayMonth && currentDate.day == holidayDay
            }
        }
    }
    
    LaunchedEffect(Unit) {
        while(true) {
            currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            delay(10_000)
        }
    }
    
    LaunchedEffect(currentSchedule?.second) {
        coroutineScope.launch {
            currentSchedule?.second?.also { index ->
                scheduleListState.scrollToItem(index)
            }
        }
    }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        columns = GridCells.Adaptive(minSize = 300.dp)
    ) {
        if(isLoadingSchedule) {
            item(
                key = "scheduleLoading",
                contentType = "loadingBar",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                )
            }
        }

        if(isScheduleLoadFailed) {
            item(
                key = "scheduleError",
                contentType = "scheduleError",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                val schedules by viewModel.schedules.collectAsState()
                
                Row(
                    modifier = Modifier
                        .background(Color.Red)
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .animateItem(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(Res.drawable.ic_error_filled),
                        tint = Color.White,
                        contentDescription = null
                    )
                    
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                            fontFamily = UustTheme.fonts.golos,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            text = if(schedules.isEmpty()) "Данные устарели" else "Ошибка загрузки расписания"
                        )

                        Text(
                            fontFamily = FontFamily(Font(Res.font.golos_text_medium)),
                            color = Color.White,
                            text = "Не удалось подключиться к сети. Данные могли успеть измениться за это время!"
                        )
                    }
                }
            }
        }
        
        item(
            key = "scheduleHeader",
            contentType = "header",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) { 
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f),
                        
                        maxLines = 1,
                        
                        autoSize = TextAutoSize.StepBased(
                            maxFontSize = 32.sp
                        ),
                        
                        fontFamily = FontFamily(
                            Font(
                                Res.font.monsterrat_italic,
                                variationSettings = FontVariation.Settings(
                                    FontVariation.weight(950)
                                )
                            )
                        ),

                        text = when(currentTime.dayOfWeek) {
                            DayOfWeek.MONDAY -> "ПОНЕДЕЛЬНИК"
                            DayOfWeek.TUESDAY -> "ВТОРНИК"
                            DayOfWeek.WEDNESDAY -> "СРЕДА"
                            DayOfWeek.THURSDAY -> "ЧЕТВЕРГ"
                            DayOfWeek.FRIDAY -> "ПЯТНИЦА"
                            DayOfWeek.SATURDAY -> "СУББОТА"
                            DayOfWeek.SUNDAY -> "ВОСКРЕСЕНИЕ"
                        }
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilledIconButton(
                            modifier = Modifier.size(32.dp),
                            shape = RoundedCornerShape(8.dp),

                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),

                            onClick = {
                                UustSettings.outlinedSchedule.toggle()
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .scale(scaleX = -1f, scaleY = 1f),

                                painter = painterResource(if(useOutlinedSchedule) { 
                                    Res.drawable.ic_outline 
                                } else Res.drawable.ic_filled),
                                    
                                contentDescription = null
                            )
                        }

                        FilledIconButton(
                            modifier = Modifier.size(32.dp),
                            shape = RoundedCornerShape(8.dp),

                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),

                            onClick = {
                                UustSettings.scheduleInRow.toggle()
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .scale(scaleX = -1f, scaleY = 1f),

                                painter = painterResource(if(showScheduleInRow) { 
                                    Res.drawable.ic_view_week_outlined 
                                } else Res.drawable.ic_table_rows_outlined),
                                    
                                contentDescription = null
                            )
                        }
                            
                        FilledIconButton(
                            modifier = Modifier.size(32.dp),
                            shape = RoundedCornerShape(8.dp),

                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),

                            onClick = {
                                backStack += Routes.Calendar
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .scale(scaleX = -1f, scaleY = 1f),

                                painter = painterResource(Res.drawable.ic_back),
                                contentDescription = null
                            )
                        }
                    }
                }

                Text(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(
                            Res.font.monsterrat_italic,
                            variationSettings = FontVariation.Settings(
                                FontVariation.weight(950)
                            )
                        )
                    ),

                    text = "${currentTime.toLocalDate().getEducationWeek()} НЕДЕЛЯ"
                )
            }
        }
        
        if(isHoliday) {
            item(
                key = "holiday",
                contentType = "holiday",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Text("ПРАЗДНИК! ОТДЫХАЕМ!")
            }
        }
        
        if(currentTime.dayOfWeek == DayOfWeek.SUNDAY) {
            item(
                key = "sunday",
                contentType = "sunday"
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontFamily = UustTheme.fonts.golos,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Выходной! Отдыхаем..."
                )
            }
        }

        item(
            key = "schedule",
            contentType = "schedule",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Crossfade(
                modifier = Modifier.animateContentSize(),
                targetState = showScheduleInRow
            ) { showScheduleInRow ->
                if(showScheduleInRow) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        contentPadding = PaddingValues(end = 32.dp),
                        state = scheduleListState,
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = scheduleListState, snapPosition = SnapPosition.Start)
                    ) {
                        itemsIndexed(
                            key = { index, _ -> index },
                            items = schedules
                        ) { _, (schedule, scheduleInfo) ->
                            val startMinute = scheduleInfo.timeFrom.hour * 60 + scheduleInfo.timeFrom.minute
                            val endMinute = scheduleInfo.timeTo.hour * 60 + scheduleInfo.timeTo.minute
                            val currentMinute = currentTime.hour * 60 + currentTime.minute
                            
                            val untilBeginning = ((startMinute - currentMinute) * 60L * 1000L).takeIf { it > 0L }
                            val untilEnding = ((endMinute - currentMinute) * 60L * 1000L).takeIf { it > 0L && it < 80 * 60 * 1000 }
                            
                            SchedulePreview(
                                modifier = Modifier.offset(x = 8.dp).width(250.dp),
                                outlined = useOutlinedSchedule,
                                type = schedule.type,
                                index = schedule.schedule_time_new_id,
                                time = schedule.schedule_time_title,
                                name = schedule.schedule_subject_title,
                                note = schedule.comment.takeIf { it.isNotBlank() },
                                location = "${schedule.building_short_title}/${schedule.room_title}",
                                teacher = schedule.teacher_fullname.takeUnless { it.isBlank() },

                                footer = untilBeginning?.let { timeBeforeBeginning ->
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
                                } ?: untilEnding?.let { timeBeforeEnd ->
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
                                    schedule == currentSchedule?.first?.first -> ScheduleItemProgress.NOW
                                    else -> ScheduleItemProgress.SOON
                                },

                                onClick = {

                                }
                            )
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        schedules.forEach { (schedule, scheduleInfo) ->
                            val startMinute = scheduleInfo.timeFrom.hour * 60 + scheduleInfo.timeFrom.minute
                            val endMinute = scheduleInfo.timeTo.hour * 60 + scheduleInfo.timeTo.minute
                            val currentMinute = currentTime.hour * 60 + currentTime.minute

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
                                location = "${schedule.building_short_title}/${schedule.room_title}",
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

        if(!hideClubAds) {
            item(
                key = "clubAdsHeader",
                contentType = "header",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                CatHeader(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                        .animateItem(),
                    icon = painterResource(Res.drawable.ic_sentiment_very_satisfied_outlined),
                    title = "Интересное"
                ) {
                    FilledIconButton(
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(8.dp),

                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),

                        onClick = {
                            UustSettings.hideClubAds.set(true)
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp)
                                .scale(scaleX = -1f, scaleY = 1f),

                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = null
                        )
                    }
                }
            }
        }
        
        if(!hideClubAds) {
            item(
                key = "clubAds",
                contentType = "ads",
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .animateItem(),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    onClick = {}
                ) {
                    Box(
                        modifier = Modifier
                            .height(112.dp)
                    ) {
                        Row {
                            Box(
                                Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .clipToBounds()
                            ) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .rotate(10f)
                                        .scale(1.2f),
                                    painter = painterResource(Res.drawable.clubs),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = UustTheme.fonts.golos,
                                    text = "Вступай в клубы!"
                                )

                                Text(
                                    fontSize = 15.sp,
                                    fontFamily = UustTheme.fonts.golos,
                                    overflow = TextOverflow.Ellipsis,
                                    text = "Проводи свободное время занимаясь любимым делом"
                                )
                            }
                        }

//                    IconButton(
//                        onClick = {}
//                    ) {
//                        Icon(
//                            painter = painterResource(Res.drawable.clos)
//                        )
//                    }
                    }
                }
            }
        }
        
        item(
            key = "lunchHeader",
            contentType = "header",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            CatHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                icon = painterResource(Res.drawable.ic_meal_outlined),
                title = "Буфет"
            ) {
                FilledIconButton(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),

                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),

                    onClick = {
//                        backStack += Routes.Calendar
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .scale(scaleX = -1f, scaleY = 1f),

                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = null
                    )
                }
            }
        }

        item(
            key = "lunchMenu",
            contentType = "lunchMenu",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LunchItem(
                    image = "https://www.nicepng.com/png/full/17-172203_free-png-sandwich-png-images-transparent-sandwich-hd.png",
                    imageScale = .75f,
                    name = "Сэндвич",
                    price = "140 руб",
                    onClick = {}
                )
                
                LunchItem(
                    image = "https://www.afina-market.ru/upload/iblock/90d/476l2f64w0v3snnirx2szv9epoj89s3c.png",
                    imageScale = .75f,
                    name = "Курник",
                    price = "75 руб",
                    onClick = {}
                )

                LunchItem(
                    image = "data:image/jpeg;base64,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",
                    imageScale = .9f,
                    name = "Компот",
                    price = "80 руб",
                    onClick = {}
                )

                LunchItem(
                    image = "https://new.lyubimiigorod.ru/images/catalog/6540d4a206cdf.png",
                    imageScale = .8f,
                    name = "Мини пицца",
                    price = "120 руб",
                    onClick = {}
                )
            } 
        }

        item(
            key = "newsHeader",
            contentType = "header",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            CatHeader(
                modifier = Modifier.padding(top = 12.dp),
                icon = painterResource(Res.drawable.ic_news_outlined),
//            secondIcon = painterResource(Res.drawable.ic_settings_outlined),
                title = "Новости"
            )
        }

        item(
            key = "newsCatsHeader",
            contentType = "newsCatsHeader",
            span = { GridItemSpan(maxLineSpan) }
        ) {
            val items = remember {
                listOf(
                    "Подписки" to Res.drawable.ic_subscriptions_outlined,
                    "Жизнь университета" to Res.drawable.ic_school_outlined,
                    "Наука" to Res.drawable.ic_science_outlined,
//                    "Научный полк" to null,
                    "Студентам" to Res.drawable.ic_diversity_3,
                    "Стипендия" to Res.drawable.ic_money,
                    "Социальная поддержка" to Res.drawable.ic_support_outlined,
                    "Общежития" to Res.drawable.ic_home_outlined,
                    "Спорт" to Res.drawable.ic_football_outlined,
                    "Отдых и досуг" to null,
                    "Трудоустройство" to null,
//                    "Сотрудникам" to null,
                    "Рейтинги" to null,
                    "Награды" to null,
                    "Партнерство" to null,
                    "СМИ о нас" to null,
//                    "Лонгриды" to null,
//                    "Другое" to null
                )
            }
            
            LazyRow(
                modifier = Modifier.padding(bottom = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEachIndexed { index, (item, icon) ->
                    item(key = item) {
                        FilterChip(
                            selected = index == 0,

                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xff723bff),
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            ),
                            
                            leadingIcon = icon?.let{{
                                Icon(
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                    painter = painterResource(icon),
                                    contentDescription = null
                                )
                            }},

                            label = {
                                Text(
                                    fontFamily = UustTheme.fonts.golos,
                                    text = item
                                )
                            },

                            onClick = {}
                        )
                    }
                }
            }
        }

        item(
            key = "newsPost1",
            contentType = "newsPost"
        ) {
            NewsPost(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                banner = "https://uust.ru/media/press-center/news/covers/2025/11/2025-11-20_16-26-26.webp",
                title = "Добро в действии: студенты УУНиТ разработали проекты волонтерского центра нового поколения",
                category = "Жизнь университета",
                categoryColor = Color(0xFFFF6C20),
                categoryIcon = Res.drawable.ic_school_outlined,
                date = "20 Ноя, 16:26",
                onClick = {}
            )
        }

        item(
            key = "newsPost2",
            contentType = "newsPost"
        ) {
            NewsPost(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                banner = "https://uust.ru/media/press-center/news/covers/2025/11/2025-11-18_09-47-23.webp",
                title = "От филологии до физики: наука и гармония династии Закирьяновых из УУНиТ",
                category = "Наука",
                categoryIcon = Res.drawable.ic_science_outlined,
                date = "18 Ноя, 09:47",
                onClick = {}
            )
        }

        item(
            key = "newsPost3",
            contentType = "newsPost"
        ) {
            NewsPost(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                banner = "https://uust.ru/media/press-center/news/covers/2025/11/2025-11-17_13-00-26.webp",
                title = "В Международный день студентов УУНиТ чествует стипендиатов Главы Башкирии",
                category = "Стипендия",
                categoryIcon = Res.drawable.ic_money,
                date = "17 Ноя, 12:45",
                onClick = {}
            )
        }
        
        item(
            key = "emptySpace",
            contentType = "emptySpace"
        ) {
            Spacer(Modifier.height(32.dp))
        }
    }
}