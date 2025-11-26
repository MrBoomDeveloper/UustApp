package com.mrboomdev.uust.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.suspendCoroutine

@Composable
actual fun rememberLocationManager(): LocationManager {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    
    return remember(context) {
        AndroidLocationManager(context).apply {
            coroutineScope.launch {
                initIfNecessary()
            }
        }
    }
}

private class AndroidLocationManager(
    private val context: Context,
): LocationManager {
    private var didInit = false
    
    private val locationManager = context.getSystemService<android.location.LocationManager>()!!
    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    
    private val _locationFlow = MutableStateFlow<Pair<Double, Double>?>(null)
    override val locationFlow = _locationFlow.asStateFlow()
    
    val _rotationFlow = MutableStateFlow(0f)
    override val rotationFlow = _rotationFlow.asStateFlow()

    private val _isEnabledFlow = MutableStateFlow(false)
    override var isEnabledFlow = _isEnabledFlow.asStateFlow()

    private val _isPreciseFlow = MutableStateFlow(false)
    override var isPreciseFlow = _isPreciseFlow.asStateFlow()
    
    suspend fun initIfNecessary() {
        if(didInit) return
        didInit = true
        
        if(context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) || 
            context.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            @SuppressLint("MissingPermission")
            init()
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private suspend fun init() {
        try {
            _locationFlow.emit(suspendCoroutine { coroutine ->
                fusedClient.lastLocation.addOnSuccessListener { location ->
                    if(location == null) {
                        coroutine.resumeWith(Result.failure(UnsupportedOperationException()))
                        return@addOnSuccessListener
                    }

                    coroutine.resumeWith(Result.success(location.latitude to location.longitude))
                }.addOnFailureListener {
                    coroutine.resumeWith(Result.failure(it))
                }.addOnCanceledListener {
                    coroutine.resumeWith(Result.failure(CancellationException()))
                }
            })
        } catch(_: UnsupportedOperationException) {
            // Retry to init
            delay(100)
            init()
            return
        }
        
        fusedClient.requestLocationUpdates(
            LocationRequest.Builder(1000)
                .setWaitForAccurateLocation(false)
                .build(), { location ->
                    @Suppress("RunBlockingInSuspendFunction") 
                    runBlocking {
                        _locationFlow.emit(location.latitude to location.longitude) 
                    } 
                }, Looper.getMainLooper()
        )
        
        while(true) {
            _isEnabledFlow.emit(LocationManagerCompat.isLocationEnabled(locationManager))
            
            _isPreciseFlow.emit(ContextCompat.checkSelfPermission(
                context, 
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)

            delay(1000)
        }
    }
}