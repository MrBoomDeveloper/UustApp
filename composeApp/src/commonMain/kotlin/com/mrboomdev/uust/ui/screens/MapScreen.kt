package com.mrboomdev.uust.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.ui.UustTheme
import com.mrboomdev.uust.ui.components.GeoMap
import com.mrboomdev.uust.ui.dialogs.LocationDialog
import com.mrboomdev.uust.ui.dialogs.LocationDialogDefaults
import com.mrboomdev.uust.utils.permissions.Permission
import com.mrboomdev.uust.utils.permissions.rememberPermissionController
import com.mrboomdev.uust.utils.rememberLocationManager
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import uust.composeapp.generated.resources.Res
import uust.composeapp.generated.resources.ic_location_filled

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
    var showLocationDialog by rememberSaveable { mutableStateOf(false) }
    var customLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    
    LaunchedEffect(Unit) {
        while(true) {
            hasLocationPermission = permissionsController.hasPermission(Permission.LOCATION)
            delay(1000)
        }
    }

    if(showLocationDialog) {
        LocationDialog(
            onDismissRequest = { showLocationDialog = false },
            onSelected = { location ->
                customLocation = if(location == LocationDialogDefaults.LIVE_LOCATION) null else location
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        GeoMap(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),

            originPosition = remember {
                54.8178296 to 56.0833728
            },

            myPosition = location
        )

        Text(
            "Current location: ${
            when {
                !hasLocationPermission -> "No permission granted!"
                !locationManager.isEnabledFlow.collectAsState().value -> "Not enabled!"
                !locationManager.isPreciseFlow.collectAsState().value -> "Not precise!"
                else -> location?.let { "${it.first}, ${it.second}" } ?: "Unknown"
            }
        }", color = Color.Green)

        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),

            icon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.ic_location_filled),
                    contentDescription = null
                )
            },

            text = {
                Text(
                    fontFamily = UustTheme.fonts.golos,
                    text = "Мое местоположение"
                )
            },

            onClick = {
                showLocationDialog = true
            }
        )
    }
}