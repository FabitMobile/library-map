package ru.fabit.map.internal.domain.entity


data class MapCoordinates
@JvmOverloads
constructor(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var speed: Double = 0.0,
    var provider: String? = null,
    var accuracy: Float = 0.0f
)