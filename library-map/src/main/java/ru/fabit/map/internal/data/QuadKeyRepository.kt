package ru.fabit.map.internal.data

import ru.fabit.map.internal.domain.entity.QuadKey
import ru.fabit.map.dependencies.storage.LocalStorageLibService

class QuadKeyRepository(private val localStorageService: LocalStorageLibService) {

    companion object {
        const val VERSION_CODE_CURRENT = "1"
        const val VERSION_CODE_KEY = "VERSION_CODE_KEY"
        const val QUAD_KEY = "QUAD_KEY"
        const val VERSION_KEY = "VERSION_KEY"
        const val PREV_VERSION_KEY = "PREV_VERSION_KEY"
        const val DEFAULT_VERSION = "0"
    }

    fun isExist(quadKey: QuadKey, version: String): Boolean {
        val map = localStorageService.getMap<String, String>(
            QUAD_KEY,
            String::class.java,
            String::class.java
        )

        val v = map.get(quadKey.toString())
        return v != null && v == version
    }

    fun addQuadKey(quadKey: QuadKey, version: String) {
        val map = localStorageService.getMap<String, String>(
            QUAD_KEY,
            String::class.java,
            String::class.java
        )
        map.put(quadKey.toString(), version)
        localStorageService.setMap(QUAD_KEY, map)
    }

    fun clearQuadKeys() {
        localStorageService.remove(QUAD_KEY)
    }

    fun versionCode(): String {
        return localStorageService.getData(
            VERSION_CODE_KEY,
            DEFAULT_VERSION
        )
    }

    fun version(): String {
        return localStorageService.getData(
            VERSION_KEY,
            DEFAULT_VERSION
        )
    }

    fun prevVersion(): String {
        return localStorageService.getData(
            PREV_VERSION_KEY,
            DEFAULT_VERSION
        )
    }

    fun changeVersion(newVersion: String) {
        val version = localStorageService.getData(
            VERSION_KEY,
            DEFAULT_VERSION
        )
        localStorageService.saveData(PREV_VERSION_KEY, version)
        localStorageService.saveData(VERSION_KEY, newVersion)
        localStorageService.saveData(VERSION_CODE_KEY, VERSION_CODE_CURRENT)
    }
}