package com.morpho.butterfly

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import net.harawata.appdirs.AppDirsFactory
import java.nio.file.Files.createDirectories
import java.nio.file.Paths.get

actual fun getPlatformCache(): CacheStorage {
    val cacheDir = AppDirsFactory.getInstance()
        .getUserCacheDir("com.morpho.app", "0.1.0", "Morpho")
    val cacheFile = createDirectories(get(cacheDir)).toFile()
    return FileStorage(cacheFile)
}