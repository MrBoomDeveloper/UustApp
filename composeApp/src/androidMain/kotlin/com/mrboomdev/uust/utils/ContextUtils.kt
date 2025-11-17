package com.mrboomdev.uust.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasPermission(name: String) = 
    ContextCompat.checkSelfPermission(
        this, 
        name
    ) == PackageManager.PERMISSION_GRANTED