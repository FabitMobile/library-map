package ru.fabit.map.internal.domain.entity


data class VisibleMapRegion(
    var topLeft: MapPoint,
    val topRight: MapPoint,
    val bottomLeft: MapPoint,
    val bottomRight: MapPoint,
    val zoom: Float
)