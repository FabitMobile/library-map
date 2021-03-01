package ru.fabit.map.internal.domain.entity.marker

import ru.fabit.map.internal.domain.entity.marker.MarkerData

enum class LoadMapItem constructor(load: Int) {

    UNKNOWN(0),
    LOW(1),
    MIDDLE(2),
    HARD(3);

    companion object {
        @JvmStatic
        fun valueOf(data: MarkerData): LoadMapItem {
            var load = 0
            if (data.freeSpaces != null
                && data.spaces != null
                && data.spaces > 10) {
                load = 1
                if (data.freeSpaces in 0..5) {
                    load = 3
                } else if (data.freeSpaces in 6..9) {
                    load = 2
                }
            } else if (data.ratio != null) {
                load = 1
                if (data.ratio in 0..50) {
                    load = 3
                } else if (data.ratio in 51..75) {
                    load = 2
                }
            }
            return valueOf(
                load
            )
        }

        @JvmStatic
        fun valueOf(load: Int): LoadMapItem {
            return when (load) {
                0 -> UNKNOWN
                1 -> LOW
                2 -> MIDDLE
                3 -> HARD
                else -> {
                    UNKNOWN
                }
            }
        }

    }


}
