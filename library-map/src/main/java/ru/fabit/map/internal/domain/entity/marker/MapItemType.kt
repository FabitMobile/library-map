package ru.fabit.map.internal.domain.entity.marker

enum class MapItemType(private val text: String) {
    UNKNOWN("unknown"),
    PARKING("parking"),
    PARKOMAT("parkomat"),
    TERMINAL("terminal"),
    GATED("gated"),
    CLOSED("closed"),
    CLUSTER("cluster"),
    PARKING_WITH_RESERVATION("parking_with_reservation"),
    RESERVATION("reservation");

    override fun toString(): String {
        return text
    }

    companion object {
        fun fromString(text: String?): MapItemType {
            return if (text == null) {
                UNKNOWN
            } else when (text) {
                "parking" -> PARKING
                "closed" -> CLOSED
                "parkomat" -> PARKOMAT
                "terminal" -> TERMINAL
                "gated" -> GATED
                "parking_with_reservation" -> PARKING_WITH_RESERVATION
                "reservation" -> RESERVATION
                "cluster" -> CLUSTER
                "unknown" -> UNKNOWN
                else -> UNKNOWN
            }
        }
    }
}