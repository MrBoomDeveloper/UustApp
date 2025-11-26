package com.mrboomdev.uust.utils

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.TextAutoSizeDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Suppress("ComposableNaming")
@Composable
fun TextAutoSize.Companion.LocalStepBased(
    minFontSize: TextUnit = TextAutoSizeDefaults.MinFontSize,
    stepSize: TextUnit = 0.25.sp
) = TextAutoSize.StepBased(LocalTextStyle.current.fontSize, stepSize, minFontSize)