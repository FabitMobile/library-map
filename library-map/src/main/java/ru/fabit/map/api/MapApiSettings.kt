package ru.fabit.map.api

import java.util.concurrent.TimeUnit

data class MapApiSettings(
    val DEFAULT_CITY_CENTER_ZOOM: Float = 10f,
    val DEFAULT_MARKER_ZOOM: Float = 17f,
    val MINI_MARKER_ZOOM: Float = 16f,
    val DELAY_ANIMATION_MARKER_MAX: Long = TimeUnit.SECONDS.toMillis(10),
    val ZOOM_VISIBLE_ANIMATION_MARKER: Float = 17f,
    val DURATION_ANIMATION: Long = TimeUnit.SECONDS.toMillis(7),
    val COUNT_LOOP_ANIMATION: Int = 1
)