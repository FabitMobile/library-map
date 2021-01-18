package ru.fabit.map.internal.domain.pinintersection.linesegment

import ru.fabit.map.internal.domain.entity.MapPoint

class LineSegment(val mapPoint1: MapPoint, val mapPoint2: MapPoint) {

    private val line: Line

    fun intersects(lineSegment: LineSegment): Boolean {
        val myLineIntersectsSegment = line.intersects(lineSegment)
        val segmentLineIntersectsMe = lineSegment.line.intersects(this)
        return myLineIntersectsSegment && segmentLineIntersectsMe
    }

    override fun toString(): String {
        return "{" +
                "point1=" + mapPoint1 +
                ", point2=" + mapPoint2
    }

    init {
        line = Line(mapPoint1, mapPoint2)
    }
}