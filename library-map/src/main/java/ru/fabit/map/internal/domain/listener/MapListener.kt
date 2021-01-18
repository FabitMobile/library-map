package ru.fabit.map.internal.domain.listener

import ru.fabit.map.internal.domain.entity.marker.Marker
import ru.fabit.map.internal.domain.entity.MapPoint

interface MapListener {

    fun onMarkerClicked(marker: Marker): Boolean

    fun onPolyLineClicked(mapPoint: MapPoint): Boolean

    fun onMapTap(mapPoint: MapPoint)

    fun onLocationDisabled()

}

