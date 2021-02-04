package ru.fabit.map.api

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import ru.fabit.map.internal.domain.listener.*
import ru.fabit.map.internal.domain.entity.MapBounds
import ru.fabit.map.internal.domain.entity.MapCoordinates
import ru.fabit.map.internal.domain.entity.marker.Marker
import ru.fabit.map.internal.domain.pinintersection.MapRegion
import ru.fabit.map.internal.domain.pinintersection.items.BaseMapElement

interface MapApi {

    fun enableLocation(enable: Boolean)

    fun setDebugMode(isDebug: Boolean)

    fun createGeoJsonLayer()

    fun setPayableZones(payableZones: List<String>)

    fun setUniqueColorForComParking(uniqueColorForComParking: Boolean)

    fun enableMap(parentView: View)

    fun disableMap(parentView: View)

    fun init(style: String)

    fun setStyle(style: String)

    fun onStart(parentView: View)

    fun onStop()

    fun onDestroy()

    fun onPause()

    fun onCreate(savedInstanceState: Bundle?)

    fun onLowMemory()

    fun onResume()

    fun onSaveInstanceState(outState: Bundle)

    fun clearCache(id: String)

    fun updateVersionCache(time: String)

    fun setMarkers(inputMarkers: List<Marker>, currentZoom: Float)

    fun deselectAll()

    fun selectMapObject(id: Int?)

    fun selectMarker(marker: Marker?, deselectingCurrent: Boolean)

    fun centerAtCoordinate(latitude: Double, longitude: Double)

    fun centerAtCoordinateWithZoom(latitude: Double, longitude: Double, zoom: Float)

    fun moveZoomAndCenterAtCoordinate(latitude: Double, longitude: Double, zoom: Float)

    fun centerAtBounds(mapBounds: MapBounds)

    fun moveToUserLocation(mapCoordinates: MapCoordinates?)

    fun moveToUserLocation(zoom: Float, mapCoordinates: MapCoordinates?)

    fun tryMoveToUserLocation(
        zoom: Float,
        defaultCoordinates: MapCoordinates,
        mapCallback: MapCallback
    )

    fun zoomIn()

    fun zoomOut()

    fun searchIntersectedObject(
        mapElements: List<BaseMapElement>,
        pinCoordinate: MapCoordinates,
        region: MapRegion
    ): Any?

    fun setFocusRect(paddingStart: Int, paddintTop: Int, paddingRight: Int, paddingBottom: Int)

    fun radarStateChange(
        isRadarOn: Boolean,
        isColoredMarkersEnabled: Boolean
    )

    fun onDisabledChange(isDisabledOn: Boolean)

    //region ===================== listeners ======================

    //region ===================== VisibleMapRegionListener ======================

    fun addVisibleMapRegionListener(visibleMapRegionListener: VisibleMapRegionListener)

    fun removeVisibleRegionListeners()

    fun removeVisibleRegionListener(visibleMapRegionListener: VisibleMapRegionListener)

    //endregion

    //region ===================== MapListener ======================

    fun addMapListener(mapListener: MapListener)

    fun removeMapListeners()

    fun removeMapListener(mapListener: MapListener)

    //endregion

    //region ===================== MapLocationListener ======================

    fun addMapLocationListener(mapLocationListener: MapLocationListener)

    fun removeMapLocationListeners()

    fun removeMapLocationListener(mapLocationListener: MapLocationListener)

    //endregion

    //region ===================== LayoutChangeListener ======================

    fun addLayoutChangeListener(layoutChangeListener: View.OnLayoutChangeListener)

    fun removeLayoutChangeListeners()

    fun removeLayoutChangeListener(layoutChangeListener: View.OnLayoutChangeListener)

    //endregion

    //region ===================== SizeChangeListener ======================

    fun addSizeChangeListener(sizeChangeListener: SizeChangeListener)

    fun removeSizeChangeListeners()

    fun removeSizeChangeListener(sizeChangeListener: SizeChangeListener)

    //endregion

    //endregion

}