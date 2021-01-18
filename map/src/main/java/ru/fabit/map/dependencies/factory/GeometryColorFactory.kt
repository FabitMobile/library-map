package ru.fabit.map.dependencies.factory

import android.content.Context
import ru.fabit.map.internal.domain.entity.marker.Marker

interface GeometryColorFactory {

    fun getColorStrokeGeometry(context: Context, marker: Marker, isRadarOn: Boolean, isDisabledOn: Boolean): Int

    fun getColorFillGeometry(context: Context, marker: Marker, isRadarOn: Boolean, isDisabledOn: Boolean): Int
}