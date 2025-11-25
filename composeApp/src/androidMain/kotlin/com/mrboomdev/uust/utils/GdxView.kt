package com.mrboomdev.uust.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import com.badlogic.gdx.*
import com.badlogic.gdx.backends.android.*
import com.badlogic.gdx.backends.android.keyboardheight.AndroidXKeyboardHeightProvider
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Clipboard
import com.badlogic.gdx.utils.SnapshotArray

fun GdxView(
    context: Context,
    listener: ApplicationListener,
    config: AndroidApplicationConfiguration
): View {
    config.nativeLoader.load()
    val logger = AndroidApplicationLogger()
    val handler = Handler()
    val clipboard = AndroidClipboard(context)
    val keyboardHeightProvider = AndroidXKeyboardHeightProvider(context as Activity)
    
    val app = object : AndroidApplicationBase {
        private val runnables = Array<Runnable>()
        private val executedRunnables = Array<Runnable>()
        
        val mGraphics = createGraphics(this, config)
        val mInput = createInput(this, context, mGraphics.view, config)
        val mFiles = createFiles(context)
        
        override fun getApplicationListener() = listener
        override fun getGraphics() = mGraphics
        override fun getInput() = mInput
        override fun getFiles() = mFiles
        override fun getHandler() = handler
        override fun getContext() = context

        override fun getAudio(): Audio? {
            TODO("Not yet implemented")
        }
        
        override fun getRunnables() = runnables
        override fun getExecutedRunnables() = executedRunnables

        override fun runOnUiThread(runnable: Runnable?) {
            TODO("Not yet implemented")
        }

        override fun startActivity(intent: Intent?) {
            TODO("Not yet implemented")
        }

        override fun getLifecycleListeners(): SnapshotArray<LifecycleListener?>? {
            TODO("Not yet implemented")
        }

        override fun getApplicationWindow() = context.window
        override fun getWindowManager() = context.windowManager

        override fun useImmersiveMode(b: Boolean) {}

        override fun createAudio(
            context: Context?,
            config: AndroidApplicationConfiguration?
        ): AndroidAudio? {
            TODO("Not yet implemented")
        }

        override fun createInput(
            activity: Application?,
            context: Context?,
            view: Any?,
            config: AndroidApplicationConfiguration?
        ): AndroidInput? {
            return DefaultAndroidInput(this, context, graphics.view, config)
        }

        override fun getNet(): Net? {
            TODO("Not yet implemented")
        }

        override fun log(tag: String?, message: String?) {
            Log.i(tag, message.toString())
        }

        override fun log(tag: String?, message: String?, exception: Throwable?) {
            Log.i(tag, message.toString(), exception)
        }

        override fun error(tag: String?, message: String?) {
            Log.e(tag, message.toString())
        }

        override fun error(tag: String?, message: String?, exception: Throwable?) {
            Log.e(tag, message.toString(), exception)
        }

        override fun debug(tag: String?, message: String?) {
            Log.d(tag, message.toString())
        }

        override fun debug(tag: String?, message: String?, exception: Throwable?) {
            TODO("Not yet implemented")
        }

        override fun setLogLevel(logLevel: Int) {
            TODO("Not yet implemented")
        }

        override fun getLogLevel(): Int {
            TODO("Not yet implemented")
        }

        override fun setApplicationLogger(applicationLogger: ApplicationLogger?) {
            TODO("Not yet implemented")
        }

        override fun getApplicationLogger() = logger

        override fun getType(): Application.ApplicationType? {
            TODO("Not yet implemented")
        }

        override fun getVersion(): Int {
            TODO("Not yet implemented")
        }

        override fun getJavaHeap(): Long {
            TODO("Not yet implemented")
        }

        override fun getNativeHeap(): Long {
            TODO("Not yet implemented")
        }

        override fun getPreferences(name: String?): Preferences? {
            TODO("Not yet implemented")
        }

        override fun getClipboard(): Clipboard? {
            TODO("Not yet implemented")
        }

        override fun postRunnable(runnable: Runnable?) {
            TODO("Not yet implemented")
        }

        override fun exit() {
            TODO("Not yet implemented")
        }

        override fun addLifecycleListener(listener: LifecycleListener?) {
            TODO("Not yet implemented")
        }

        override fun removeLifecycleListener(listener: LifecycleListener?) {
            TODO("Not yet implemented")
        }
    }

    Gdx.app = app
    Gdx.input = app.mInput
    Gdx.files = app.mFiles
    Gdx.graphics = app.mGraphics
    
    return app.mGraphics.view
}

private fun createGraphics(
    context: AndroidApplicationBase,
    config: AndroidApplicationConfiguration
): AndroidGraphics {
    return AndroidGraphics(
        context, config,
        if(config.resolutionStrategy == null) FillResolutionStrategy() else config.resolutionStrategy
    )
}

private fun createFiles(context: ContextWrapper): AndroidFiles {
    context.filesDir // workaround for Android bug #10515463
    return DefaultAndroidFiles(context.assets, context, true)
}