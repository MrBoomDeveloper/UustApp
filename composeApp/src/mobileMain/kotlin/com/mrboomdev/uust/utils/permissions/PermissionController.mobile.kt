package com.mrboomdev.uust.utils.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.location.LOCATION
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import kotlinx.coroutines.runBlocking

@Composable
actual fun rememberPermissionController(): PermissionController {
    val permissionsControllerFactory = rememberPermissionsControllerFactory()

    val permissionsController = remember(permissionsControllerFactory) {
        permissionsControllerFactory.createPermissionsController()
    }

    BindEffect(permissionsController)
    
    return remember(permissionsController) { 
        object : PermissionController {
            override fun hasPermission(permission: Permission): Boolean {
                return runBlocking {
                    permissionsController.isPermissionGranted(when(permission) {
                        Permission.LOCATION -> dev.icerock.moko.permissions.Permission.LOCATION
                        Permission.NOTIFICATIONS -> dev.icerock.moko.permissions.Permission.REMOTE_NOTIFICATION
                    })
                }
            }

            override suspend fun requestPermission(
                permission: Permission
            ): PermissionRequestResult = try {
                permissionsController.providePermission(when(permission) {
                    Permission.LOCATION -> dev.icerock.moko.permissions.Permission.LOCATION
                    Permission.NOTIFICATIONS -> dev.icerock.moko.permissions.Permission.REMOTE_NOTIFICATION
                })

                PermissionRequestResult.GRANTED
            } catch(_: DeniedAlwaysException) { 
                PermissionRequestResult.FUCKED
            } catch(_: DeniedException) {
                PermissionRequestResult.DENIED
            } catch(_: RequestCanceledException) {
                PermissionRequestResult.CANCELLED
            }

            override fun requestPermissionFucked(permission: Permission) {
                // TODO: Open specific permission screen
                permissionsController.openAppSettings()
            }
        }
    }
}