package ru.fabit.map.internal.domain.entity

data class Rect(val minY: Double, val maxY: Double, val minX: Double, val maxX: Double) {

    val topLeft: MapPoint
        get() = MapPoint(maxY, minX)

    val topRight: MapPoint
        get() = MapPoint(maxY, maxX)

    val bottomLeft: MapPoint
        get() = MapPoint(minY, minX)

    val bottomRight: MapPoint
        get() = MapPoint(minY, maxX)

    val mediumX: Double
        get() = minX + (maxX - minX) / 2

    val mediumY: Double
        get() = minY + (maxY - minY) / 2

    val height: Double
        get() = maxY - minY

    val width: Double
        get() = maxX - minX

    fun intersects(rect: Rect): Boolean {
        return minX < rect.maxX && rect.minX < maxX && maxY > rect.minY && rect.maxY > minY
    }

    override fun toString(): String {
        return "{" +
                "minLat=" + minY +
                ", maxLat=" + maxY +
                ", minLon=" + minX +
                ", maxLon=" + maxX +
                '}'
    }
}