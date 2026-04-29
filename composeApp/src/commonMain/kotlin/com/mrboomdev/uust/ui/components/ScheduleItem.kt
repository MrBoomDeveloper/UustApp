package com.mrboomdev.uust.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrboomdev.uust.data.SubjectIcons
import com.mrboomdev.uust.resources.Res
import com.mrboomdev.uust.resources.golos_text_bold
import com.mrboomdev.uust.resources.golos_text_regular
import com.mrboomdev.uust.resources.ic_clock_outlined
import com.mrboomdev.uust.resources.ic_location_outlined
import com.mrboomdev.uust.resources.ic_notes
import com.mrboomdev.uust.resources.ic_person_outlined
import com.mrboomdev.uust.ui.isDarkTheme
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

data class ScheduleItemType(
    val darkColor: Color,
    val lightColor: Color = darkColor
) {
    @Composable
    fun getColor() = if(isDarkTheme()) {
        darkColor
    } else lightColor
}

val scheduleItemTypes = mapOf(
    "Практика (семинар)" to ScheduleItemType(
        darkColor = Color(0xFFFFA64E),
        lightColor = Color(0xFFFFA248)
    ),
    
    "Лекция" to ScheduleItemType(
        darkColor = Color(0xFFFF7AB4)
    ),
    
    "УУНиТ-СТАРТ" to ScheduleItemType(
        darkColor = Color(0xFFB07AFF)
    ),
    
    "Лабораторная работа" to ScheduleItemType(
        darkColor = Color(0xFF78A7FF),
        lightColor = Color(0xFF4184FF)
    )
)

enum class ScheduleItemProgress {
    SOON, COMPLETED, NOW
}

@Composable
fun SchedulePreview(
    modifier: Modifier = Modifier.width(250.dp),
    type: String,
    progress: ScheduleItemProgress,
    index: Int,
    time: String,
    footer: String? = null,
    outlined: Boolean = false,
    name: String,
    note: String? = null,
    location: String? = null,
    teacher: String? = null,
    onClick: () -> Unit
) {
    val foundType = scheduleItemTypes[type]
    
    Surface(
        modifier = modifier
            .alpha(if(progress == ScheduleItemProgress.COMPLETED) .5f else 1f),
        
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        
        border = BorderStroke(2.dp, if(outlined) when(progress) {
            ScheduleItemProgress.SOON,
            ScheduleItemProgress.COMPLETED -> MaterialTheme.colorScheme.surface
            ScheduleItemProgress.NOW -> foundType?.getColor() ?: MaterialTheme.colorScheme.surface
        } else MaterialTheme.colorScheme.surface),
        
        color = if(!outlined) when(progress) {
            ScheduleItemProgress.SOON, 
            ScheduleItemProgress.COMPLETED -> MaterialTheme.colorScheme.surface
            ScheduleItemProgress.NOW -> foundType?.getColor() ?: MaterialTheme.colorScheme.surface
        } else MaterialTheme.colorScheme.surface,
        
        contentColor = if(!outlined) when(progress) {
            ScheduleItemProgress.SOON,
            ScheduleItemProgress.COMPLETED -> contentColorFor(MaterialTheme.colorScheme.surface)
            ScheduleItemProgress.NOW -> Color.Black
        } else contentColorFor(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    color = if(progress == ScheduleItemProgress.NOW) {
                        Color.Unspecified
                    } else foundType?.getColor() ?: Color.Unspecified,

                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(Font(Res.font.golos_text_bold)),
                    text = buildString { 
                        append(index)
                        append(". ")
                        append(type)
                        append("  ")
                    }
                )
                
                Text(
                    color = if(progress == ScheduleItemProgress.NOW) {
                        Color.Unspecified
                    } else foundType?.getColor() ?: Color.Unspecified,

                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(Font(Res.font.golos_text_bold)),
                    text = time
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val fontStyle = MaterialTheme.typography.bodyLarge
                val subjectIcon = SubjectIcons[name]
                
                Text(
                    style = fontStyle,
                    fontFamily = FontFamily(Font(Res.font.golos_text_bold)),
                    
                    inlineContent = mapOf(
                        "icon" to InlineTextContent(
                            placeholder = Placeholder(
                                width = fontStyle.fontSize,
                                height = fontStyle.fontSize,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                            )
                        ) {
                            if(subjectIcon != null) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .scale(1.4f * subjectIcon.iconScale),
                                    
                                    painter = painterResource(if(progress == ScheduleItemProgress.NOW) {
                                        subjectIcon.activeIcon
                                    } else subjectIcon.icon),
                                    
                                    contentDescription = null
                                )
                            }
                        }
                    ),
                    
                    text = buildAnnotatedString { 
                        if(subjectIcon != null) {
                            appendInlineContent("icon")
                            append("  ")
                        }
                        
                        append(name)
                    }
                )
            }

            Spacer(Modifier.weight(1f, false))

            note?.also {
                Text(
                    modifier = Modifier.alpha(.9f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(Font(Res.font.golos_text_regular)),

                    inlineContent = mapOf(
                        "icon" to InlineTextContent(
                            placeholder = Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.Center),
                            children = {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(Res.drawable.ic_notes),
                                    contentDescription = null
                                )
                            }
                        )
                    ),

                    text = buildAnnotatedString {
                        appendInlineContent("icon")
                        append(" ")
                        append(note)
                    }
                )
            }
            
            teacher?.also {
                Text(
                    modifier = Modifier.alpha(.9f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(Font(Res.font.golos_text_regular)),
                    
                    inlineContent = mapOf(
                        "icon" to InlineTextContent(
                            placeholder = Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.Center),
                            children = {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(Res.drawable.ic_person_outlined),
                                    contentDescription = null
                                )
                            }
                        )
                    ),
                    
                    text = buildAnnotatedString { 
                        appendInlineContent("icon")
                        append(" ")
                        append(teacher)
                    }
                )
            }

            location?.also {
                Text(
                    modifier = Modifier.alpha(.9f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(Font(Res.font.golos_text_regular)),

                    inlineContent = mapOf(
                        "icon" to InlineTextContent(
                            placeholder = Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.Center),
                            children = {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(Res.drawable.ic_location_outlined),
                                    contentDescription = null
                                )
                            }
                        )
                    ),

                    text = buildAnnotatedString {
                        appendInlineContent("icon")
                        append(" ")
                        append(location)
                    }
                )
            }
            
            footer?.also { 
                HorizontalDivider(
                    modifier = Modifier
                        .alpha(.5f)
                        .padding(top = 8.dp, bottom = 4.dp)
                )
                
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(.9f),
                    
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(Font(Res.font.golos_text_regular)),
                    
                    inlineContent = mapOf(
                        "icon" to InlineTextContent(
                            placeholder = Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.Center),
                            children = {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(Res.drawable.ic_clock_outlined),
                                    contentDescription = null
                                )
                            }
                        )
                    ),
                    
                    text = buildAnnotatedString {
                        appendInlineContent("icon")
                        append(" ")
                        append(footer)
                    }
                )
            }
        }
    }
}