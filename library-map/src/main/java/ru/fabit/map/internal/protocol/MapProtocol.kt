package ru.fabit.map.internal.protocol

import android.os.Bundle
import android.view.View
import ru.fabit.map.internal.domain.listener.*
import ru.fabit.map.internal.domain.entity.MapCoordinates
import ru.fabit.map.internal.domain.entity.marker.Marker
import ru.fabit.map.internal.domain.entity.Rect
import ru.fabit.map.internal.domain.entity.marker.AnimationMarkerListener
import ru.fabit.map.internal.domain.entity.MapBounds

interface MapProtocol {

    fun isDebugMode(): Boolean = false

    fun setDebugMode(isDebug: Boolean) {}

    fun setPayableZones(payableZones: List<String>) {}

    fun setUniqueColorForComParking(uniqueColorForComParking: Boolean) {}

    fun createGeoJsonLayer() {}

    fun disableMap() {}

    fun enableMap() {}

    fun init(style: String) {}

    fun start() {}

    fun stop() {}

    fun getMapView(): View? = null

    fun setFocusRect(topLeftX: Float, topLeftY: Float, bottomRightX: Float, bottomRightY: Float) {}

    fun clearCache(id: String) {}

    fun updateVersionCache(time: String) {}

    fun destroy() {}

    fun create(savedInstanceState: Bundle?) {}

    //region ===================== Listener ======================

    //region ===================== VisibleMapRegionListener ======================

    fun addVisibleMapRegionListener(visibleMapRegionListener: VisibleMapRegionListener) {}

    fun removeVisibleRegionListeners() {}

    fun removeVisibleRegionListener(visibleMapRegionListener: VisibleMapRegionListener) {}

    //endregion

    //region ===================== VisibleMapRegionListener ======================

    fun addMapListener(mapListener: MapListener) {}

    fun removeMapListeners() {}

    fun removeMapListener(mapListener: MapListener) {}

    //endregion

    //region ===================== MapLocationListener ======================

    fun addMapLocationListener(mapLocationListener: MapLocationListener) {}

    fun removeMapLocationListeners() {}

    fun removeMapLocationListener(mapLocationListener: MapLocationListener) {}

    //endregion

    //region ===================== LayoutChangeListener ======================

    fun addLayoutChangeListener(layoutChangeListener: View.OnLayoutChangeListener) {}

    fun removeLayoutChangeListeners() {}

    fun removeLayoutChangeListener(layoutChangeListener: View.OnLayoutChangeListener) {}

    //endregion

    //region ===================== SizeChangeListener ======================

    fun addSizeChangeListener(sizeChangeListener: SizeChangeListener) {}

    fun removeSizeChangeListeners() {}

    fun removeSizeChangeListener(sizeChangeListener: SizeChangeListener) {}

    //endregion


    //endregion


    fun drawQuad(key: String, rect: Rect, color: Int) {}

    fun enableLocation(enable: Boolean?) {}

    @JvmSuppressWildcards
    fun insert(markers: List<Marker>, zoom: Float) {}

    @JvmSuppressWildcards
    fun remove(markers: List<Marker>) {}

    @JvmSuppressWildcards
    fun update(markers: List<Marker>, zoom: Float) {}

    fun moveCameraPosition(latitude: Double, longitude: Double) {}

    fun moveCameraPositionWithZoom(latitude: Double, longitude: Double, zoom: Float) {}

    fun moveCameraPositionWithBounds(mapBounds: MapBounds) {}

    fun moveCameraZoomAndPosition(latitude: Double, longitude: Double, zoom: Float) {}

    fun moveToUserLocation(defaultCoordinates: MapCoordinates? = null) {}

    fun moveToUserLocation(zoom: Float, defaultCoordinates: MapCoordinates? = null) {}

    fun tryMoveToUserLocation(
        zoom: Float,
        defaultCoordinates: MapCoordinates,
        mapCallback: MapCallback
    ) {}

    fun deselect(markerToDeselect: Marker) {}

    fun selectMarker(markerToSelect: Marker) {}

    fun zoomIn() {}

    fun zoomOut() {}

    fun getVisibleRegionByZoomAndPoint(zoom: Float, latitude: Double, longitude: Double): MapBounds

    @JvmSuppressWildcards
    fun radarStateChange(
        isRadarOn: Boolean,
        isColoredMarkersEnabled: Boolean,
        markers: Collection<Marker>
    ) {}

    fun onDisabledChange(isDisabledOn: Boolean) {}

    fun setAnimationMarkerListener(animationMarkerListener: AnimationMarkerListener) {}

    fun setStyle(style: String) {}
    fun resume() {}
    fun pause() {}
    fun saveInstanceState(outState: Bundle) {}
    fun onLowMemory() {}
    fun isAnimatedMarkersEnabled(): Boolean
}