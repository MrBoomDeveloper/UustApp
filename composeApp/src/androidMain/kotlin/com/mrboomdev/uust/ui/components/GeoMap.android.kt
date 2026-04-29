package com.mrboomdev.uust.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.mrboomdev.uust.utils.GdxView

@Suppress("NOTHING_TO_INLINE")
private inline fun Double.asGameCoordinate() = (this * 1000000).toFloat()

@Composable
actual fun GeoMap(
    modifier: Modifier,
    originPosition: Pair<Double, Double>,
    myPosition: Pair<Double, Double>?
) {
    val context = LocalActivity.current!!

    val game = remember(context) {
        object : ApplicationListener {
            private val thisGame = this

            private lateinit var batch: ModelBatch
            private lateinit var environment: Environment
            private lateinit var camera: PerspectiveCamera

            private lateinit var myModel: ModelInstance
            private lateinit var modelTexture: Texture

            private lateinit var floorModel: ModelInstance
            private lateinit var floorTexture: Texture

            private var myPosition = myPosition ?: originPosition
            private var originPosition = originPosition

            private var playerAnimationProgress = 3f
            private var playerTargetAnimationUp = true

            var currentZoom = 25f

            override fun create() {
                batch = ModelBatch()

                environment = Environment().apply {
                    set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
                    add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
                }

                myModel = ModelBuilder().createSphere(
                    5f, 5f, 5f, 10, 10,
                    Material(TextureAttribute.createDiffuse(Texture(Gdx.files.internal("bocchi.png")).also {
                        modelTexture = it
                    })),
                    (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong()
                ).let { ModelInstance(it) }.apply {
                    transform.setToRotation(Vector3.Y, 90f)
                    transform.rotate(Vector3.X, -15f)

//                    transform.setTranslation(
//                        originPosition.first.asGameCoordinate(),
//                        6f,
//                        originPosition.second.asGameCoordinate()
//                    )
                }

                floorModel = ModelBuilder().createBox(
                    500f, 1f, 500f,
                    Material(TextureAttribute.createDiffuse(Texture(Gdx.files.internal("wallpaper.jpg")).also {
                        floorTexture = it
                    })),
                    (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong()
                ).let { ModelInstance(it) }

                camera = PerspectiveCamera(
                    67f,
                    Gdx.graphics.width.toFloat(),
                    Gdx.graphics.height.toFloat()
                ).apply {
                    near = 1f
                    far = 1000f
                }

                Gdx.input.inputProcessor = object : InputAdapter() {
                    var lastDistance = -1f

                    var lastX = -1
                    var lastY = -1

                    var lastX2 = -1
                    var lastY2 = -1

                    var pointer = -1
                    var pointer2 = -1

                    override fun touchDown(
                        screenX: Int,
                        screenY: Int,
                        pointer: Int,
                        button: Int
                    ): Boolean {
                        if(this.pointer == -1) {
                            this.pointer = pointer
                            this.lastX = screenX
                            this.lastY = screenY
                            return true
                        }

                        if(this.pointer2 == -1) {
                            this.pointer2 = pointer
                            this.lastX2 = screenX
                            this.lastY2 = screenY
                            return true
                        }

                        return false
                    }

                    override fun touchUp(
                        screenX: Int,
                        screenY: Int,
                        pointer: Int,
                        button: Int
                    ): Boolean {
                        if(pointer == this.pointer2) {
                            this.pointer2 = -1
                            this.lastX2 = -1
                            this.lastY2 = -1
                            this.lastDistance = -1f
                            return true
                        }

                        if(pointer == this.pointer) {
                            this.pointer = -1
                            this.lastX = -1
                            this.lastY = -1
                            this.lastDistance = -1f
                            return true
                        }

                        return false
                    }

                    override fun touchDragged(
                        screenX: Int,
                        screenY: Int,
                        pointer: Int
                    ): Boolean {
                        if(this.pointer != -1 && this.pointer2 != -1 && (pointer == this.pointer || pointer == this.pointer2)) {
                            when(pointer) {
                                this.pointer -> {
                                    lastX = screenX
                                    lastY = screenY
                                }

                                this.pointer2 -> {
                                    lastX2 = screenX
                                    lastY2 = screenY
                                }
                            }

                            val distance = Vector2(lastX.toFloat(), lastY.toFloat()).dst(
                                Vector2(
                                    lastX2.toFloat(),
                                    lastY2.toFloat()
                                )
                            )
                            if(lastDistance == -1f) lastDistance = distance
                            currentZoom = Math.clamp(currentZoom - (distance - lastDistance) * .0005f, 1f, 50f)

                            return true
                        }

                        if(pointer == this.pointer) {
                            // Drag camera
                            thisGame.myPosition.also { oldPosition ->
                                thisGame.myPosition = oldPosition.copy(
                                    first = oldPosition.first - (screenX - this.lastX) / 10000000.0,
                                    second = oldPosition.second - (screenY - this.lastY) / 10000000.0
                                )
                            }

                            // Save position
                            this.lastX = screenX
                            this.lastY = screenY
                            return true
                        }

                        return false
                    }
                }
            }

            override fun resize(width: Int, height: Int) {}

            override fun render() {
                update()
                ScreenUtils.clear(Color.BLACK, true)

                batch.begin(camera)
                batch.render(floorModel, environment)
                batch.render(myModel, environment)
                batch.end()
            }

            private fun update() {
                this.myPosition.also { position ->
                    val wasMyPosition = myModel.transform.getTranslation(Vector3())
                    val relativeX = (position.first - this.originPosition.first).asGameCoordinate()
                    val relativeY = (position.second - this.originPosition.second).asGameCoordinate()
                     
                    myModel.transform.setToRotation(Vector3.Y, 90f)
                    myModel.transform.rotate(Vector3.Z, -15f)

                    myModel.transform.setTranslation(
                        relativeX + (wasMyPosition.x - relativeX) * 0.05f,
                        playerAnimationProgress,
                        relativeY + (wasMyPosition.y - relativeY) * 0.05f
                    )
                }

                myModel.transform.getTranslation(Vector3()).also { modelPosition ->
                    camera.position.x = modelPosition.x
                    camera.position.y = modelPosition.y + (currentZoom * 10)
                    camera.position.z = modelPosition.z + 100f
                    camera.lookAt(modelPosition)
                    camera.update()
                }

                playerAnimationProgress += ((if(playerTargetAnimationUp) 4f else 3f) - playerAnimationProgress) * .025f

                if(playerTargetAnimationUp && playerAnimationProgress >= 3.9f) {
                    playerTargetAnimationUp = false
                } else if(!playerTargetAnimationUp && playerAnimationProgress <= 3.1f) {
                    playerTargetAnimationUp = true
                }
            }

            fun updateProps(
                originPosition: Pair<Double, Double>,
                myPosition: Pair<Double, Double>?
            ) {
                if(myPosition != null) {
                    this.myPosition = myPosition
                }

                this.originPosition = originPosition
                println("update props")
            }

            override fun pause() {}
            override fun resume() {}

            override fun dispose() {
                batch.dispose()

                floorTexture.dispose()
                modelTexture.dispose()

                myModel.model.dispose()
                floorModel.model.dispose()
            }
        }
    }

    val gdxView = remember(context, game) {
        GdxView(
            context = context,
            config = AndroidApplicationConfiguration(),
            listener = game
        )
    }

    AndroidView(
        modifier = modifier,
        factory = { gdxView },
        update = {
            game.updateProps(
                originPosition = originPosition,
                myPosition = myPosition
            )
        }
    )
}