package ru.fabit.map.dependencies.factory

import ru.fabit.map.internal.domain.entity.MapBounds


interface GeoJsonFactory {

    fun createGeoJson(mapBounds: MapBounds): ByteArray
}