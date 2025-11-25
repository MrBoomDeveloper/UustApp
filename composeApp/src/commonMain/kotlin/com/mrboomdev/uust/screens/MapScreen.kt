package com.mrboomdev.uust.screens

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
import com.mrboomdev.uust.components.GeoMap
import com.mrboomdev.uust.utils.permissions.Permission
import com.mrboomdev.uust.utils.permissions.PermissionRequestResult
import com.mrboomdev.uust.utils.permissions.rememberPermissionController
import com.mrboomdev.uust.utils.rememberLocationManager
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
            hasLocationPermission = permissionsController.hasPermission(Permission.LOCATION)
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
                        permissionsController.requestPermissionFucked(Permission.LOCATION)
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
        GeoMap(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            
            me = location
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            if(!hasLocationPermission) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            when(permissionsController.requestPermission(
                                Permission.LOCATION
                            )) {
                                PermissionRequestResult.GRANTED -> {
                                    hasLocationPermission = true
                                }
                                
                                PermissionRequestResult.DENIED -> {
                                    error = "Permission denied!"
                                }
                                
                                PermissionRequestResult.FUCKED -> {
                                    error = "Permission denied and cannot be asked for! Grant it in app's settings."
                                }
                                
                                PermissionRequestResult.CANCELLED -> {}
                            }
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