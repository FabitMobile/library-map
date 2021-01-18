package ru.fabit.map.internal.domain.pinintersection.linesegment

import ru.fabit.map.internal.domain.entity.MapPoint
import ru.fabit.map.internal.domain.entity.Rect

data class Polygon(private val boundingRect: Rect, private val segments: List<LineSegment>) {


    operator fun contains(mapPoint: MapPoint): Boolean {
        val segmentUp = LineSegment(
            mapPoint,
            MapPoint(mapPoint.longitude, boundingRect.minY - boundingRect.height * 0.5)
        )
        var intersectionsCount = 0
        for (segment in segments) {
            if (segment.intersects(segmentUp)) {
                intersectionsCount += 1
            }
        }
        return intersectionsCount % 2 != 0
    }
}