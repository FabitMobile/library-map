package ru.fabit.map.api.internal

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import ru.fabit.map.api.MapApi
import ru.fabit.map.internal.domain.entity.*
import ru.fabit.map.internal.domain.entity.marker.*
import ru.fabit.map.internal.domain.listener.*
import ru.fabit.map.internal.domain.pinintersection.MapRegion
import ru.fabit.map.internal.domain.pinintersection.PinIntersector
import ru.fabit.map.internal.domain.pinintersection.items.BaseMapElement
import ru.fabit.map.internal.protocol.MapProtocol
import java.util.*
import java.util.concurrent.TimeUnit

internal class MapApiImpl(
    private val mapProtocol: MapProtocol,
    private val getCurrentTime: () -> Long,
    private val cleanParkOutDate: (ids: List<Int>?) -> Unit
) : MapApi, AnimationMarkerListener {

    private var idObjectForSelected: Int? = null
    private val DEFAULT_MARKER_ZOOM = 17f
    private val MINI_MARKER_ZOOM = 16f
    private val selectedMarkers: MutableMap<String, Marker>
    private val markers: MutableMap<String, Marker>
    private val pinIntersector: PinIntersector
    private val DELAY_ANIMATION_MARKER_MAX = TimeUnit.SECONDS.toMillis(10)
    private val ZOOM_VISIBLE_ANIMATION_MARKER = 17f

    init {
        this.pinIntersector = PinIntersector()
        this.selectedMarkers = HashMap()
        this.markers = HashMap()
        this.mapProtocol.setAnimationMarkerListener(this)
    }

    override fun enableLocation(enable: Boolean) {
        mapProtocol.enableLocation(enable)
    }

    override fun setDebugMode(isDebug: Boolean) {
        mapProtocol.setDebugMode(isDebug)
    }

    override fun setPayableZones(payableZones: List<String>) {
        mapProtocol.setPayableZones(payableZones)
    }

    override fun setUniqueColorForComParking(uniqueColorForComParking: Boolean) {
        mapProtocol.setUniqueColorForComParking(uniqueColorForComParking)
    }

    override fun createGeoJsonLayer() {
        mapProtocol.createGeoJsonLayer()
    }

    override fun enableMap(parentView: View) {
        mapProtocol.enableMap()
        if (parentView is ViewGroup) {
            val isContains = mapViewIsContainedIn(parentView)
            if (!isContains) {
                val layout = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                mapProtocol.getMapView()?.layoutParams = layout
            }
            parentView.addView(mapProtocol.getMapView(), 0)
        }
    }

    override fun disableMap(parentView: View) {
        if (parentView is ViewGroup) {
            for (i in 0..parentView.childCount) {
                if (parentView.getChildAt(i) == mapProtocol.getMapView()) {
                    parentView.removeViewAt(i)
                }
            }
        }
        mapProtocol.disableMap()
    }

    override fun setFocusRect(
        paddingStart: Int,
        paddintTop: Int,
        paddingRight: Int,
        paddingBottom: Int
    ) {
        if (paddingBottom == 0) {
            return
        }
        val mapWidth = mapProtocol.getMapView()?.width ?: 0
        val mapHeight = mapProtocol.getMapView()?.height ?: 0

        val newValue = mapHeight - paddingBottom

        val mapViewRect = Rect()
        mapProtocol.getMapView()?.getDrawingRect(mapViewRect)
        if (newValue > 0 && mapViewRect.contains(
                0,
                0,
                mapWidth,
                newValue
            )
        ) {
            mapProtocol.setFocusRect(0f, 0f, mapWidth.toFloat(), newValue.toFloat())
        }
    }

    override fun radarStateChange(isRadarOn: Boolean, isColoredMarkersEnabled: Boolean) {
        mapProtocol.radarStateChange(isRadarOn, isColoredMarkersEnabled, markers.values)
    }

    override fun onDisabledChange(isDisabledOn: Boolean) {
        mapProtocol.onDisabledChange(isDisabledOn)
    }

    override fun init(style: String) {
        mapProtocol.init(style)
    }

    override fun setStyle(style: String) {
        mapProtocol.setStyle(style)
    }

    override fun onStart(parentView: View) {
        if (parentView is ViewGroup) {
            val isContains = mapViewIsContainedIn(parentView)
            if (!isContains) {
                val parent = mapProtocol.getMapView()?.parent
                parent?.let {
                    if (it is ViewGroup) {
                        it.removeView(mapProtocol.getMapView())
                    }
                }
                parentView.addView(mapProtocol.getMapView(), 0)
            }
        }
        mapProtocol.start()
    }

    override fun onStop() {
        mapProtocol.stop()
    }

    override fun onDestroy() {
        mapProtocol.destroy()
    }

    override fun onPause() {
        mapProtocol.pause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mapProtocol.create(savedInstanceState)
    }

    override fun onLowMemory() {
        mapProtocol.onLowMemory()
    }

    override fun onResume() {
        mapProtocol.resume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mapProtocol.saveInstanceState(outState)
    }

    override fun clearCache(id: String) {
        mapProtocol.clearCache(id)
    }

    override fun updateVersionCache(time: String) {
        mapProtocol.updateVersionCache(time)
    }

    override fun setMarkers(inputMarkers: List<Marker>, currentZoom: Float) {
        val newMarkers = HashMap<String, Marker>()
        val animMarkers = HashMap<String, Marker>()
        inputMarkers.forEach {
            if (currentZoom < DEFAULT_MARKER_ZOOM && currentZoom >= MINI_MARKER_ZOOM && it.type == MarkerType.DEFAULT) {
                it.type = MarkerType.SMALL
            }
            newMarkers[it.id] = it

            if (it.state == MarkerState.SELECTED) {
                selectedMarkers[it.id] = it
                selectMarker(it, true)
            }

            animMarkers.putAll(
                createAnimationMarker(newMarkers.values, currentZoom)
            )
        }

        calculateDiff(markers, mapOf(*newMarkers.toList().toTypedArray(), *animMarkers.toList().toTypedArray() ), currentZoom)
        this.markers.clear()
        this.markers.putAll(newMarkers)
        idObjectForSelected?.let { selectMapObject(it) }
    }

    private fun createAnimationMarker(
        markers: Collection<Marker>,
        currentZoom: Float
    ): Map<String, AnimationMarker> {

        val markerAnimations = HashMap<String, AnimationMarker>()

        if (mapProtocol.radarStatus() && currentZoom >= ZOOM_VISIBLE_ANIMATION_MARKER) {
            for (marker in markers) {

                val mapItem = marker.data as MarkerData
                val loadMapItem =
                    LoadMapItem.valueOf(
                        mapItem
                    )

                if (loadMapItem != LoadMapItem.UNKNOWN || loadMapItem != LoadMapItem.LOW) {
                    if (mapItem.timeStampParkOut != null && mapItem.timeStampParkOut != 0L
                        && getCurrentTime() - mapItem.timeStampParkOut < TimeUnit.MINUTES.toMillis(
                            1
                        )
                    ) {
                        if (MapItemType.fromString(mapItem.type) == MapItemType.CLOSED
                            || MapItemType.fromString(mapItem.type) == MapItemType.GATED
                            || MapItemType.fromString(mapItem.type) == MapItemType.PARKING
                        ) {
                            if (mapItem.location?.type == Location.LINE_STRING) {
                                val lines = mapItem.location.lineStringMapCoordinates
                                createAnimationMarker(
                                    markerAnimations,
                                    marker,
                                    mapItem,
                                    lines,
                                    lines.size
                                )
                            } else if (mapItem.location?.type == Location.POLYGON) {
                                val polygon = mapItem.location.polygonMapCoordinate
                                if (polygon != null && polygon.size > 0) {
                                    val lines = polygon[0]
                                    val lineSize = lines.size
                                    createAnimationMarker(
                                        markerAnimations,
                                        marker,
                                        mapItem,
                                        lines,
                                        lineSize
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        return markerAnimations
    }

    private fun createAnimationMarker(
        markerAnimations: MutableMap<String, AnimationMarker>,
        marker: Marker,
        mapItem: MarkerData,
        lines: List<MapCoordinates>,
        size: Int
    ) {
        if (size >= 2) {
            val point1 = Point(lines[0].latitude, lines[0].longitude)
            val point2 = Point(lines[1].latitude, lines[1].longitude)

            val animationMarker =
                createAnimationMarkerVehicleLeaving(marker, mapItem, point1, point2)
            markerAnimations[animationMarker.id] = animationMarker
        }
    }

    private fun createAnimationMarkerVehicleLeaving(
        marker: Marker,
        mapItem: MarkerData,
        lineA: Point,
        lineB: Point
    ): AnimationMarker {
        val DURATION_VEHICLE_LEAVING_ANIMATION = TimeUnit.SECONDS.toMillis(7)
        val COUNT_LOOP_VEHICLE_LEAVING_ANIMATION = 1

        var x = (lineA.longitude + lineB.longitude) / 2
        var y = (lineA.latitude + lineB.latitude) / 2

        x = (x + lineB.longitude) / 2
        y = (y + lineB.latitude) / 2
        val animationMarkerType =
            AnimationMarkerType.VEHICLE_LEAVING
        val markerAnimation = AnimationMarker(
            marker.id + animationMarkerType.toString(),
            y,
            x
        )
        markerAnimation.animationMarkerType =
            AnimationMarkerType.VEHICLE_LEAVING
        markerAnimation.data = mapItem
        markerAnimation.type = MarkerType.ANIMATION
        markerAnimation.countLoop = COUNT_LOOP_VEHICLE_LEAVING_ANIMATION
        markerAnimation.duration = DURATION_VEHICLE_LEAVING_ANIMATION

        val leftLimit = 0L
        val rightLimit = DELAY_ANIMATION_MARKER_MAX
        val generatedLong = leftLimit + (Math.random() * (rightLimit - leftLimit)).toLong()

        markerAnimation.delay = generatedLong
        return markerAnimation
    }

    override fun deselectAll() {
        for (selectedMarker in selectedMarkers.values) {
            selectedMarker.state =
                MarkerState.DEFAULT
            if (selectedMarker.type != MarkerType.ANIMATION) {
                this.mapProtocol.deselect(selectedMarker)
            }
        }
        selectedMarkers.clear()
    }

    override fun selectMapObject(id: Int?) {
        if (id != null) {
            idObjectForSelected = id
            for (marker in markers.values) {
                val newMapItem = marker.data
                if (newMapItem?.id == id) {
                    selectMarker(marker, true)
                    mapProtocol.moveCameraPosition(marker.latitude, marker.longitude)
                }
            }
        }
    }

    override fun selectMarker(marker: Marker?, deselectingCurrent: Boolean) {
        if (marker != null && marker.type != MarkerType.ANIMATION) {
            if (deselectingCurrent) {
                deselectAll()
            }
            marker.state = MarkerState.SELECTED
            this.mapProtocol.selectMarker(marker)
            this.selectedMarkers[marker.id] = marker
            idObjectForSelected = null
        }
    }

    override fun centerAtCoordinate(latitude: Double, longitude: Double) {
        mapProtocol.moveCameraPosition(latitude, longitude)
    }

    override fun centerAtCoordinateWithZoom(latitude: Double, longitude: Double, zoom: Float) {
        mapProtocol.moveCameraPositionWithZoom(latitude, longitude, zoom)
    }

    override fun moveZoomAndCenterAtCoordinate(latitude: Double, longitude: Double, zoom: Float) {
        mapProtocol.moveCameraZoomAndPosition(latitude, longitude, zoom)
    }

    override fun centerAtBounds(mapBounds: MapBounds) {
        mapProtocol.moveCameraPositionWithBounds(mapBounds)
    }

    override fun moveToUserLocation(mapCoordinates: MapCoordinates?) {
        mapProtocol.moveToUserLocation(mapCoordinates)
    }

    override fun moveToUserLocation(zoom: Float, mapCoordinates: MapCoordinates?) {
        mapProtocol.moveToUserLocation(zoom, mapCoordinates)
    }

    override fun tryMoveToUserLocation(
        zoom: Float,
        defaultCoordinates: MapCoordinates,
        mapCallback: MapCallback
    ) {
        mapProtocol.tryMoveToUserLocation(zoom, defaultCoordinates, mapCallback)
    }

    override fun zoomIn() {
        mapProtocol.zoomIn()
    }

    override fun zoomOut() {
        mapProtocol.zoomOut()
    }

    override fun searchIntersectedObject(
        mapElements: List<BaseMapElement>,
        pinCoordinate: MapCoordinates,
        region: MapRegion
    ): Any? {
        return pinIntersector.searchIntersectedObject(mapElements, pinCoordinate, region)
    }

    //AnimationMarkerListener

    override fun onAnimationStop(animationMarker: AnimationMarker) {
    }

    //region ===================== Listener ======================


    //region ===================== VisibleMapRegionListener ======================

    override fun addVisibleMapRegionListener(visibleMapRegionListener: VisibleMapRegionListener) {
        mapProtocol.addVisibleMapRegionListener(visibleMapRegionListener)
    }

    override fun removeVisibleRegionListeners() {
        mapProtocol.removeVisibleRegionListeners()
    }

    override fun removeVisibleRegionListener(visibleMapRegionListener: VisibleMapRegionListener) {
        mapProtocol.removeVisibleRegionListener(visibleMapRegionListener)
    }

    //endregion

    //region ===================== MapListener ======================

    override fun addMapListener(mapListener: MapListener) {
        mapProtocol.addMapListener(mapListener)
    }

    override fun removeMapListeners() {
        mapProtocol.removeMapListeners()
    }

    override fun removeMapListener(mapListener: MapListener) {
        mapProtocol.removeMapListener(mapListener)
    }

    //endregion

    //region ===================== MapLocationListener ======================


    override fun addMapLocationListener(mapLocationListener: MapLocationListener) {
        mapProtocol.addMapLocationListener(mapLocationListener)
    }

    override fun removeMapLocationListeners() {
        mapProtocol.removeMapLocationListeners()
    }

    override fun removeMapLocationListener(mapLocationListener: MapLocationListener) {
        mapProtocol.removeMapLocationListener(mapLocationListener)
    }

    //endregion

    //region ===================== LayoutChangeListener ======================

    override fun addLayoutChangeListener(layoutChangeListener: View.OnLayoutChangeListener) {
        mapProtocol.addLayoutChangeListener(layoutChangeListener)
    }

    override fun removeLayoutChangeListeners() {
        mapProtocol.removeMapLocationListeners()
    }

    override fun removeLayoutChangeListener(layoutChangeListener: View.OnLayoutChangeListener) {
        mapProtocol.removeLayoutChangeListener(layoutChangeListener)
    }

    //endregion

    //region ===================== SizeChangeListener ======================

    override fun addSizeChangeListener(sizeChangeListener: SizeChangeListener) {
        mapProtocol.addSizeChangeListener(sizeChangeListener)
    }

    override fun removeSizeChangeListeners() {
        mapProtocol.removeSizeChangeListeners()
    }

    override fun removeSizeChangeListener(sizeChangeListener: SizeChangeListener) {
        mapProtocol.removeSizeChangeListener(sizeChangeListener)
    }

    //endregion

    //endregion

    //region ===================== Internal logic ======================

    private fun calculateDiff(
        oldMarkers: MutableMap<String, Marker>,
        newMarkers: Map<String, Marker>,
        zoom: Float
    ) {
        val toAdd = ArrayList<Marker>()
        val toRemove = ArrayList<Marker>()
        val toUpdate = ArrayList<Marker>()
        val cleanParkingIdsParkOut = ArrayList<Int>()

        searchIntersectionNewInOld(oldMarkers, newMarkers, toAdd, toUpdate)

        searchIntersectionOldInNew(oldMarkers, newMarkers, toRemove, cleanParkingIdsParkOut)

        if (toRemove.count() > 0) {
            toRemove.forEach {
                oldMarkers.remove(it.id)
            }
            mapProtocol.remove(toRemove)
        }

        if (toAdd.count() > 0) {
            toAdd.forEach {
                oldMarkers.put(it.id, it)
            }
            mapProtocol.insert(toAdd, zoom)
        }

        if (toUpdate.count() > 0) {
            toUpdate.forEach {
                oldMarkers[it.id] = it
            }
            mapProtocol.update(toUpdate, zoom)
        }

        if (cleanParkingIdsParkOut.size > 0) {
            cleanParkOutDate(cleanParkingIdsParkOut)
        }
    }

    private fun searchIntersectionOldInNew(
        oldMarkers: Map<String, Marker>,
        newMarkers: Map<String, Marker>,
        toRemove: MutableList<Marker>,
        cleanParkingIdsParkOut: MutableList<Int>
    ) {
        for ((key, value) in oldMarkers) {

            val mapObjectToRemove = newMarkers[key]
            if (mapObjectToRemove == null) {
                if (value.type == MarkerType.ANIMATION) {
                    cleanParkingIdsParkOut.add((value.data as MarkerData).id)
                } else {
                    toRemove.add(value)
                }
            } else {
                val markerTypeEquals = mapObjectToRemove.type == value.type
                if (!markerTypeEquals) {
                    toRemove.add(value)
                }
                if (mapObjectToRemove.type == MarkerType.ANIMATION) {
                    val removeAnimationMarker = mapObjectToRemove as AnimationMarker?

                    val markerAnimationStop =
                        removeAnimationMarker?.animationMarkerState == AnimationMarkerState.STOP

                    if (markerAnimationStop) {
                        toRemove.add(value)
                        cleanParkingIdsParkOut.add((value.data as MarkerData).id)
                    }
                }
            }
        }
    }

    private fun searchIntersectionNewInOld(
        oldMarkers: Map<String, Marker>,
        newMarkers: Map<String, Marker>,
        toAdd: MutableList<Marker>,
        toUpdate: MutableList<Marker>
    ) {
        for ((key, value) in newMarkers) {

            val mapObjectToAdd = oldMarkers[key]

            if (mapObjectToAdd == null) {
                toAdd.add(value)
            } else {

                val oldData = mapObjectToAdd.data
                val newData = value.data

                val areEquals = oldData?.freeSpaces == newData?.freeSpaces

                if (!areEquals) {
                    toUpdate.add(value)
                }

                val markerTypeEquals = mapObjectToAdd.type == value.type
                if (!markerTypeEquals) {
                    toAdd.add(value)
                }

            }
        }
    }

    private fun mapViewIsContainedIn(view: ViewGroup): Boolean {
        var isContains = false
        for (i in 0..view.childCount) {
            if (view.getChildAt(i) == mapProtocol.getMapView()) {
                isContains = true
                break
            }
        }
        return isContains
    }

    ///
}