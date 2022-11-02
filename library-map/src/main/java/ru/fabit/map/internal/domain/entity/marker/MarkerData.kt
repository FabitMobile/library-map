package ru.fabit.map.internal.domain.entity.marker

import ru.fabit.map.internal.domain.entity.Location

data class MarkerData(
    val id: Int,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val priceWithCurrency: String?,
    val spaces: Int?,
    val freeSpaces: Int?,
    val blocked: Boolean?,
    val type: String,
    val location: Location?,
    val total: Int?,
    val listLocation: List<Location>,
    val iconName: String?,
    val zoom: Int?,
    val zoneNumber: String?,
    val minPrice: Int?,
    val maxPrice: Int?,
    val handicapped: Int?,
    val percentFreeSpaces: Int?,
    val timeStampParkOut: Long? = -1L,
    val city: String? = null
)