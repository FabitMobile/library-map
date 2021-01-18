package ru.fabit.map.internal.domain.pinintersection.linesegment

import ru.fabit.map.internal.domain.entity.MapPoint

internal class Line(mapPoint1: MapPoint, mapPoint2: MapPoint) {

    var paramA: Double
    var paramB: Double
    var paramC: Double

    fun intersects(lineSegment: LineSegment): Boolean {
        val firstPart = paramA * lineSegment.mapPoint1.longitude + paramB * lineSegment.mapPoint1.latitude + paramC
        val secondPart = paramA * lineSegment.mapPoint2.longitude + paramB * lineSegment.mapPoint2.latitude + paramC
        return firstPart * secondPart < 0
    }

    init {
        paramA = mapPoint1.latitude - mapPoint2.latitude
        paramB = mapPoint2.longitude - mapPoint1.longitude
        paramC = mapPoint1.longitude * mapPoint2.latitude - mapPoint2.longitude * mapPoint1.latitude
    }
}