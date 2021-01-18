package ru.fabit.map.internal.domain.listener

import ru.fabit.map.internal.domain.entity.LocationStatus
import ru.fabit.map.internal.domain.entity.MapCoordinates

interface MapLocationListener {

    fun onLocationStatusUpdate(status: LocationStatus)

    fun onLocationUpdate(location: MapCoordinates)
}