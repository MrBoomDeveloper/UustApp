plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.mrboomdev.uust.android"
    compileSdk = libs.versions.android.targetSdk.get().toInt()

    sourceSets {
        named("main") {
            jniLibs.srcDirs("libs")
        }
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

val natives: Configuration by configurations.creating

dependencies {
    val gdxVersion = "1.14.0"
    natives("com.badlogicgames.gdx:gdx:${gdxVersion}")
    natives("com.badlogicgames.gdx:gdx-backend-android:${gdxVersion}")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
}

// Called every time gradle gets executed, takes the native dependencies of
// the natives configuration, and extracts them to the proper libs/ folders
// so they get packed with the APK.
tasks.register("copyAndroidNatives") {
    doFirst {
        natives.files.forEach { jar ->
            val outputDir = file("libs/" + jar.nameWithoutExtension.substringAfterLast("natives-"))
            outputDir.mkdirs()
            
            copy {
                from(zipTree(jar))
                into(outputDir)
                include("*.so")
            }
        }
    }
}

tasks.whenTaskAdded {
    if("package" in name) {
        dependsOn("copyAndroidNatives")
    }
}