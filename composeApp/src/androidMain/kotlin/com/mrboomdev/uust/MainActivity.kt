package com.mrboomdev.uust

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composer
import androidx.compose.runtime.tooling.ComposeStackTraceMode
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mrboomdev.uust.navigation.Routes
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.skeptick.libres.LibresSettings
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        Uust.setContext(newBase)
        LibresSettings.languageCode = "ru"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        
        if(BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
            Composer.setDiagnosticStackTraceMode(ComposeStackTraceMode.SourceInformation)
        }

        Thread.setDefaultUncaughtExceptionHandler { _, t ->
            startActivity(Intent(this, MainActivity::class.java).apply { 
                putExtra("error", t.stackTraceToString())
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })

            Process.killProcess(Process.myPid())
            exitProcess(10)
        }
        
        val initialRoute = intent?.getStringExtra("error")?.let { 
            Routes.Crash(it)
        } ?: Routes.Home

        setContent {
            App(initialRoute)
        }
    }
}