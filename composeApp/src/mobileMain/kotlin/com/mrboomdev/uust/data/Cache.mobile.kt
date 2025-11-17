package com.mrboomdev.uust.data

import io.github.vinceglb.filekit.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

actual object Cache {
    actual suspend fun <T> save(
        fileName: String, 
        serializer: KSerializer<T>, 
        value: T
    ) {
        FileKit.cacheDir.resolve("uust/$fileName").also { file ->
            if(file.parent() == null) {
                file.resolve("..").createDirectories(mustCreate = true)
            }
            
            file.writeString(Json.encodeToString(serializer, value))
        }
    }

    actual suspend fun <T> load(
        fileName: String,
        serializer: KSerializer<T>
    ): T? = FileKit.cacheDir.resolve("uust/$fileName").takeIf {
        it.exists() 
    }?.let { 
        Json.decodeFromString(serializer, it.readString())
    }
}