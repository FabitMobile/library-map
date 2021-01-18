package ru.fabit.map.internal.domain.listener

import ru.fabit.map.internal.domain.entity.VisibleMapRegion

interface VisibleMapRegionListener {

    fun onRegionChange(visibleMapRegion: VisibleMapRegion)
}