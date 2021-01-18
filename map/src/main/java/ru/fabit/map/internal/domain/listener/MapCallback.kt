package ru.fabit.map.internal.domain.listener

import ru.fabit.map.internal.domain.entity.MapCoordinates

interface MapCallback {
    fun onCameraMoved(coordinates: MapCoordinates)
}
