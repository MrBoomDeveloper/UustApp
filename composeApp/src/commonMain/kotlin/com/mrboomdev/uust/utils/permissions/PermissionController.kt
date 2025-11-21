package com.mrboomdev.uust.utils.permissions

import androidx.compose.runtime.Composable

interface PermissionController {
    fun hasPermission(permission: Permission): Boolean
    
    suspend fun requestPermission(
        permission: Permission
    ): PermissionRequestResult

    /**
     * Opens system settings (ofter user has to search for the app to grant an permission)
     */
    fun requestPermissionFucked(permission: Permission)
}

enum class PermissionRequestResult {
    GRANTED, DENIED, FUCKED, CANCELLED
}

@Composable
expect fun rememberPermissionController(): PermissionController