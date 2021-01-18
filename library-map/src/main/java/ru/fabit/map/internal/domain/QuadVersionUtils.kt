package ru.fabit.map.internal.domain

import ru.fabit.map.internal.data.QuadKeyRepository
import kotlin.math.abs

fun getVersion(quadKeyRepository: QuadKeyRepository, id: String): String {
    return "${quadKeyRepository.versionCode()}.${quadKeyRepository.version()}.${abs(id.hashCode())}"
}

fun getPrevVersion(quadKeyRepository: QuadKeyRepository, id: String): String {
    return "${quadKeyRepository.versionCode()}.${quadKeyRepository.prevVersion()}.${abs(id.hashCode())}"
}