package com.mrboomdev.uust.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mrboomdev.uust.utils.GdxView
import javax.microedition.khronos.opengles.GL10

@Composable
actual fun GeoMap(
    modifier: Modifier, 
    me: Pair<Double, Double>?
) {
    val context = LocalContext.current
    
    val gdxView = remember(context) {
        GdxView(
            context = context,
            
            listener = object : ApplicationListener {
                private lateinit var batch: SpriteBatch
                private lateinit var texture: Texture
                
                override fun create() {
                    batch = SpriteBatch()
                    texture = Texture(Gdx.files.internal("anime.jpg"))
                }

                override fun resize(width: Int, height: Int) {}

                override fun render() {
                    batch.begin()
                    batch.draw(texture, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
                    batch.end()
                }

                override fun pause() {}
                override fun resume() {}

                override fun dispose() {
                    batch.dispose()
                }
            },
            
            config = AndroidApplicationConfiguration().apply {
                useImmersiveMode = true
            }
        )
    }

    AndroidView(
        modifier = modifier,
        factory = { gdxView }
    )
}

private class GdxApplication: ApplicationListener {
    private val spritebatch = SpriteBatch()
    
    override fun render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT);
        
        spritebatch.begin()
//        model.render(GL10.GL_TRIANGLES);
        spritebatch.end()
    }

    override fun dispose() {
        spritebatch.dispose()
    }

    override fun create() {}
    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
}