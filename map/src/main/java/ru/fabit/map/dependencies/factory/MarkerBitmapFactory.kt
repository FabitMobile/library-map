package ru.fabit.map.dependencies.factory

import android.content.Context
import android.graphics.Bitmap
import ru.fabit.map.internal.domain.entity.marker.Marker

interface MarkerBitmapFactory {

    fun getBitmapMapObjectId(context: Context, marker: Marker, isRadarOn: Boolean, isDisabledOn: Boolean, isColoredMarkersEnabled: Boolean, needUniqueColor: Boolean): String

    fun getBitmapMapObject(context: Context, marker: Marker, isRadarOn: Boolean, isDisabledOn: Boolean, isColoredMarkersEnabled: Boolean, needUniqueColor: Boolean): Bitmap?
}