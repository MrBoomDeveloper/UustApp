import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    applyDefaultHierarchyTemplate()
    
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

//    listOf(
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation("org.jetbrains.compose.components:components-resources:1.10.0-beta02")
            implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.0-beta02")
            implementation(libs.compose.material3)
            implementation(libs.compose.adaptive)

            implementation(libs.navigation3.ui)
            implementation(libs.navigation3.viewmodel)
            
            implementation(libs.compose.viewmodel)
            implementation(libs.compose.lifecycle)

            implementation(libs.filekit.core)
            implementation(libs.settings)
            implementation(libs.settings.coroutines)
            implementation(libs.settings.noarg)
            
            implementation(libs.napier)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

            implementation(libs.humanReadable)
        }

        val mobileMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.permissions)
                implementation(libs.permissions.location)
                implementation(libs.permissions.notifications)
                implementation(libs.permissions.compose)
            }
        }
        
        androidMain {
            dependsOn(mobileMain)
            dependencies {
                implementation(project(":android"))
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.appcompat)
                implementation(libs.android.splashscreen)
                implementation("com.google.android.gms:play-services-location:21.3.0")

                val gdxVersion = "1.14.0"
                implementation("com.badlogicgames.gdx:gdx:${gdxVersion}")
                implementation("com.badlogicgames.gdx:gdx-backend-android:${gdxVersion}")
            }
        }

//        iosMain {
//            dependsOn(mobileMain)
//        }
        
        webMain.dependencies {
            implementation(libs.settings.makeobservable)
            implementation(libs.ktor.client.js)
        }
        
        wasmJsMain.dependencies {
            implementation("com.github.zakgof:korender:0.5.1")
        }
        
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.mrboomdev.uust"
    compileSdk = libs.versions.android.targetSdk.get().toInt()

    defaultConfig {
        applicationId = namespace
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 3
        versionName = "1.1.1"
        
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86_64")
            abiFilters.add("x86")
        }
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = false
        }
        
        debug { 
            applicationIdSuffix = ".debug"
        }
    }
    
    buildFeatures {
        buildConfig = true
        compose = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}