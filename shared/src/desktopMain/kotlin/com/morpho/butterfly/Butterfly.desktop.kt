package com.morpho.butterfly

import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import net.harawata.appdirs.AppDirsFactory
import java.nio.file.Files
import java.nio.file.Paths

actual fun getPlatformCache(): CacheStorage {
    val cacheDir = AppDirsFactory.getInstance()
        .getUserCacheDir("com.morpho.app", "0.1.0", "Morpho")
    val cacheFile = Files.createDirectories(Paths.get(cacheDir)).toFile()
    return FileStorage(cacheFile)
}