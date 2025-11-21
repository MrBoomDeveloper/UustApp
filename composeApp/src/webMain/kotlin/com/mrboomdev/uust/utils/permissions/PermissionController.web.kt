package com.mrboomdev.uust.utils.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun rememberPermissionController(): PermissionController {
    return remember { 
        object : PermissionController {
            override fun hasPermission(permission: Permission): Boolean {
                return false
            }

            override suspend fun requestPermission(permission: Permission): PermissionRequestResult {
                alertNotImplemented()
                return PermissionRequestResult.DENIED
            }

            override fun requestPermissionFucked(permission: Permission) {
                alertNotImplemented()
            }
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun alertNotImplemented() {
    js("alert(\"Not yet implemented\")")
}