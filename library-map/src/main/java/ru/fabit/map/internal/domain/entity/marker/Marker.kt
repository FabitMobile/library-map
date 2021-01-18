package ru.fabit.map.internal.domain.entity.marker

open class Marker(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val relativeObjects: MutableList<Any> = mutableListOf(),
    var state: MarkerState = MarkerState.DEFAULT,
    var type: MarkerType = MarkerType.DEFAULT,
    var data: MarkerData? = null,
    var iconWidth: Int = 0,
    var iconHeight: Int = 0
) {

    fun addRelativeObject(relativeObject: Any) {
        relativeObjects.add(relativeObject)
    }

    fun removeRelativeObjects() {
        relativeObjects.clear()
    }
}