package ru.fabit.map.internal.domain.entity.marker


enum class MarkerType(val markerType: String) {
    NO_MARKER("no_marker"),
    DEFAULT("default"),
    SMALL("small"),
    ANIMATION("animation");

    override fun toString(): String {
        return markerType
    }
}
