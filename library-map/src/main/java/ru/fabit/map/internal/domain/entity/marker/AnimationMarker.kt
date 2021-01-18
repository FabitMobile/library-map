package ru.fabit.map.internal.domain.entity.marker

class AnimationMarker
@JvmOverloads
constructor(id: String,
            latitude: Double,
            longitude: Double,
            var delay: Long = 0,
            var duration: Long = 0,
            var countLoop: Int = 0,
            var animationIcon: Any? = null,
            var animationMarkerState: AnimationMarkerState = AnimationMarkerState.HIDEN,
            var animationMarkerType: AnimationMarkerType = AnimationMarkerType.UNKNOWN
) : Marker(id, latitude, longitude)