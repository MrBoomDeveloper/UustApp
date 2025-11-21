package com.mrboomdev.uust.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mrboomdev.uust.utils.rememberLocationManager
import com.mrboomdev.uust.utils.rememberPermissionController
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    contentPadding: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()
    val locationManager = rememberLocationManager()
    val permissionsController = rememberPermissionController()
    val isLocationEnabled by locationManager.isEnabledFlow.collectAsState()
    val isLocationPrecise by locationManager.isPreciseFlow.collectAsState()
    val location by locationManager.locationFlow.collectAsState()
    var hasLocationPermission by remember { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    
    LaunchedEffect(Unit) {
        while(true) {
            hasLocationPermission = permissionsController.isPermissionGranted(Permission.LOCATION)
            delay(1000)
        }
    }

    error?.also { theError ->
        AlertDialog(
            onDismissRequest = { error = null },
            title = { Text("Error has occurred!") },
            text = { Text(theError) },
            
            dismissButton = {
                TextButton(
                    onClick = {
                        permissionsController.openAppSettings()
                    }
                ) {
                    Text("Open app settings")
                }
            },
            
            confirmButton = {
                TextButton(
                    onClick = {
                        error = null
                    }
                ) {
                    Text("Ok")
                }
            }
        )
    }
    
    Box(Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            location?.also { location ->
                drawCircle(
                    color = Color.Red,
                    radius = 10f,
                    //center = Offset(location.first.toFloat(), location.second.toFloat())
                )
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            if(!hasLocationPermission) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                permissionsController.providePermission(Permission.LOCATION)
                                hasLocationPermission = true
                            } catch(_: DeniedAlwaysException) {
                                error = "Permission denied and cannot be asked for! Grant it in app's settings."
                            } catch(_: DeniedException) {
                                error = "Permission denied!"
                            } catch(_: RequestCanceledException) {}
                        }
                    }
                ) {
                    Text("Request location permission")
                }
            }

            Text("Current location: ${when {
                !hasLocationPermission -> "No permission granted!"
                !locationManager.isEnabledFlow.collectAsState().value -> "Not enabled!"
                !locationManager.isPreciseFlow.collectAsState().value -> "Not precise!"
                else -> location?.let { "${it.first}, ${it.second}" } ?: "Unknown"
            }}", color = Color.Green)
        }
    }
}