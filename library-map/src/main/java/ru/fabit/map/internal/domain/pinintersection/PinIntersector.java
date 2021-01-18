package ru.fabit.map.internal.domain.pinintersection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.fabit.map.internal.domain.entity.MapCoordinates;
import ru.fabit.map.internal.domain.entity.MapPoint;
import ru.fabit.map.internal.domain.entity.Rect;
import ru.fabit.map.internal.domain.entity.marker.Marker;
import ru.fabit.map.internal.domain.pinintersection.items.BaseMapElement;
import ru.fabit.map.internal.domain.pinintersection.items.MapMarker;
import ru.fabit.map.internal.domain.pinintersection.items.MapPolygon;
import ru.fabit.map.internal.domain.pinintersection.items.MapPolyline;
import ru.fabit.map.internal.domain.pinintersection.linesegment.LineSegment;
import ru.fabit.map.internal.domain.pinintersection.linesegment.Polygon;

public class PinIntersector {


    public PinIntersector() {
    }

    //region ===================== Protocol ======================

    public MapRegion pinRegionByPoint(MapCoordinates point, MapRegion region, double multiplier) {

        double latWidth = region.latWidth();
        double lonWidth = region.lonWidth();
        double centerSide = Collections.min(Arrays.asList(latWidth, lonWidth)) * multiplier;
        double centerSide2 = centerSide / 2;

        double pinRegMinLon = point.getLongitude() - centerSide2;
        double pinRegMaxLon = point.getLongitude() + centerSide2;

        double pinRegMinLat = point.getLatitude() - centerSide2;
        double pinRegMaxLat = point.getLatitude() + centerSide2;

        MapRegion pinReg = new MapRegion(pinRegMinLat, pinRegMaxLat, pinRegMinLon, pinRegMaxLon);

        return pinReg;
    }

    public MapRegion pinRegion(WorldCoordinate centerCoordinate, MapRegion region, double multiplier) {

        double latWidth = region.latWidth();
        double lonWidth = region.lonWidth();
        double centerSide = Collections.min(Arrays.asList(latWidth, lonWidth)) * multiplier;
        double centerSide2 = centerSide / 2;

        double pinRegMinLon = centerCoordinate.getLng() - centerSide2;
        double pinRegMaxLon = centerCoordinate.getLng() + centerSide2;

        double pinRegMinLat = centerCoordinate.getLat() - centerSide2;
        double pinRegMaxLat = centerCoordinate.getLat() + centerSide2;

        MapRegion pinReg = new MapRegion(pinRegMinLat, pinRegMaxLat, pinRegMinLon, pinRegMaxLon);

        return pinReg;

    }

    public MapRegion markerPinRegion(WorldCoordinate centerCoordinate, MapRegion region, int iconWidth, int iconHeight, double screenWidth, double screenHeight) {
        double latWidth = region.latWidth();
        double lonWidth = region.lonWidth();

        double latSide = latWidth * (iconWidth / screenWidth) * 0.8;
        double latSide2 = latSide / 2;

        double lngSide = lonWidth * (iconHeight / screenHeight) * 0.5;
        double lngSide2 = lngSide / 2;


        double pinRegMinLon = centerCoordinate.getLng() - latSide2;
        double pinRegMaxLon = centerCoordinate.getLng() + latSide2;

        double pinRegMinLat = centerCoordinate.getLat() - lngSide2;
        double pinRegMaxLat = centerCoordinate.getLat() + lngSide2;

        MapRegion pinReg = new MapRegion(pinRegMinLat, pinRegMaxLat, pinRegMinLon, pinRegMaxLon);

        return pinReg;

    }


    public void searchIntersected(List<BaseMapElement> mapElements,
                                  WorldCoordinate pinCoordinate,
                                  MapRegion region,
                                  double screenWidth,
                                  double screenHeight,
                                  Callback callback) {
        MapRegion pinRegionForLines = pinRegion(pinCoordinate, region, 0.035);
        MapRegion pinRegionForMarker = pinRegion(pinCoordinate, region, 0.005);

        List<MapMarker> markers = new ArrayList<>();
        List<MapPolyline> polylines = new ArrayList<>();
        List<MapPolygon> polygons = new ArrayList<>();

        for (BaseMapElement baseMapElement : mapElements) {
            if (baseMapElement instanceof MapMarker) {
                markers.add((MapMarker) baseMapElement);
            } else if (baseMapElement instanceof MapPolyline) {
                polylines.add((MapPolyline) baseMapElement);
            } else if (baseMapElement instanceof MapPolygon) {
                polygons.add((MapPolygon) baseMapElement);
            }
        }

        MapPolyline mapPolyline = searchInPolylines(polylines, pinRegionForLines);
        MapPolygon mapPolygon = searchInPolygons(polygons, pinRegionForLines);
        MapMarker mapMarker = searchInMarkers(markers, pinRegionForMarker, region, screenWidth, screenHeight, callback);


        MapMarker marker = null;
        String stateGroupIdentifier = null;

        if (mapPolyline != null) {
            stateGroupIdentifier = mapPolyline.getStateGroupIdentifier();
        } else if (mapPolygon != null) {
            stateGroupIdentifier = mapPolygon.getStateGroupIdentifier();
        } else if (mapMarker != null) {
            stateGroupIdentifier = mapMarker.getStateGroupIdentifier();
        }

        if (stateGroupIdentifier != null) {
            for (MapMarker item : markers) {
                if (item.getStateGroupIdentifier() != null
                        && item.getStateGroupIdentifier().equals(stateGroupIdentifier)) {
                    marker = item;
                    break;
                }
            }
        }

        if (callback != null) {
            callback.onFinish(marker);
        }
    }

    public Object searchIntersectedObject(List<BaseMapElement> mapElements,
                                          MapCoordinates pinCoordinate,
                                          MapRegion region) {
        MapRegion pinRegionForLines = pinRegionByPoint(pinCoordinate, region, 0.035);

        List<MapPolyline> polylines = new ArrayList<>();
        List<MapPolygon> polygons = new ArrayList<>();

        for (BaseMapElement baseMapElement : mapElements) {
            if (baseMapElement instanceof MapPolyline) {
                polylines.add((MapPolyline) baseMapElement);
            } else if (baseMapElement instanceof MapPolygon) {
                polygons.add((MapPolygon) baseMapElement);
            }
        }

        MapPolyline mapPolyline = searchInPolylines(polylines, pinRegionForLines);
        MapPolygon mapPolygon = searchInPolygons(polygons, pinRegionForLines);

        Object object = null;
        if (mapPolyline != null) {
            object = mapPolyline.getUserData();
        } else if (mapPolygon != null) {
            object = mapPolygon.getUserData();
        }

        return object;
    }


    //endregion

    //region ===================== Internal logic ======================

    // MARK: - markers
    private MapMarker searchInMarkers(List<MapMarker> mapMarkers, MapRegion pinRegion, MapRegion mapRegion, double screenWidth, double screenHeight, Callback callback) {
        MapMarker result = null;

        for (MapMarker marker : mapMarkers) {

            WorldCoordinate worldCoordinate = marker.getCoordinate();

            Marker userData = (Marker) marker.getUserData();
            MapRegion markerRegion = this.markerPinRegion(worldCoordinate, mapRegion, userData.getIconWidth(), userData.getIconWidth(), screenWidth, screenHeight);

            if (pinRegion.intersects(markerRegion)) {
                result = marker;
                break;
            }
        }

        return result;
    }

    /*
     *  LineSegment (отрезок)
     *  1. координаты двух крайних точек отрезка
     *  2. уравнение прямой, содержащей отрезок
     *
     *  Алгоритм определения пересения линии с прямоугольником
     *  Ищем пересечение линии с парковки с каждой из сторон прямоугольника под пином автомобиля.
     *  Если пересекается хотя бы с одной стороной - считаем, что нашли.
     *
     *  1. получаем pinRect
     *  2. составляем LineSegment для каждой из сторон (pinRectTop, ...Bottom, ...Left, ...Right)
     *  3. для каждой полилинии (polyline)
     *  3.1. составляем список из LineSegment этой полилинии
     *  3.2. для каждого сегмента (segment)
     *  3.2.1. для каждой стороны pinRect (pinRectSide)
     *  3.2.2.1. проверяем пересечение сегмента
     *  3.2.2.1.1 если да, то мы нашли пересечение отрезка с одной из сторон pinRect. Заканчиваем все циклы.
     *
     *  Уравнение прямой описывается как Ax + By + C = 0, Если проходит чере две точки (x1,y1) и (x2,y2), то можно посчитать коэффициенты
     *  A = y1 - y2
     *  B = x2 - x1
     *  C = x1y2 - x2y1
     *
     *  отрезок - часть прямой, ограниченная двумя точками (x1,y1) и (x2,y2)
     *  прямая пересекает отрезок, если (Ax1 + By1 + C) * (Ax2 + By2 + C) < 0
     *
     *  два отрезка segment1 и segment2 пересекаются, если
     *  1. прямая, которой принадлежит segment1, пересекает segment2
     *  2. прямая, которой принадлежит segment2, пересекает segment1
     *
     */

    private MapPolyline searchInPolylines(List<MapPolyline> polylines, MapRegion pinRegion) {
        List<LineSegment> pinRectSegments = makeSegments(pinRegion.makeRect());

        for (MapPolyline polyline : polylines) {
            for (LineSegment segment : makeSegments(polyline.getWorldCoordinates())) {
                for (LineSegment pinRectSegment : pinRectSegments) {
                    if (segment.intersects(pinRectSegment)) {
                        return polyline;
                    }
                }
            }
        }
        return null;
    }

    // MARK: - polygons
    /*
     *  Алгоритм определения принаджелжности точки многоугольнику
     *  Стреляем из точки лучем, ищем пересечения этого луча со всеми ребрами
     *  Если количество переечений нечетное, значит точка внутри
     *
     *  1. получаем pinRect и его середину pinPoint (нам нужна точка в этом случае)
     *  2. для каждого полигона (polygon)
     *  3.1. составляем список из LineSegment его сторон и создаем Polygon
     *  3.2. узнаем регион полигона polygonRect
     *  3.3. создаем отрезок вверх: начало в точке pinPoint, конец в точке (pinPoint.x, polygonRect.minY)
     *  3.4. ищем пересечения
     *  3.5. если нечетное количество - мы внутри
     *
     */
    private MapPolygon searchInPolygons(List<MapPolygon> mapPolygons,
                                        MapRegion pinRegion) {
        Rect pinRect = pinRegion.makeRect();
        MapPoint pinMapPoint = new MapPoint(pinRect.getMediumY(), pinRect.getMediumX());

        MapPolygon result = null;

        for (MapPolygon polygon : mapPolygons) {

            Polygon polygonFigure = new Polygon(polygon.makeBoundingRegion().makeRect(),
                    makeSegments(polygon.getWorldCoordinates())
            );

            if (polygonFigure.contains(pinMapPoint)) {
                result = polygon;
                break;
            }
        }
        return result;
    }

    private List<LineSegment> makeSegments(List<WorldCoordinate> coordinates) {
        List<LineSegment> segments = new ArrayList<>();
        int size = coordinates.size();

        for (int i = 0; i < size - 1; i++) {
            WorldCoordinate coord1 = coordinates.get(i);
            WorldCoordinate coord2 = coordinates.get(i + 1);
            segments.add(
                    new LineSegment(
                            new MapPoint(coord1.getLat(), coord1.getLng()),
                            new MapPoint(coord2.getLat(), coord2.getLng())
                    )
            );
        }
        return segments;
    }


    private List<LineSegment> makeSegments(Rect rect) {

        List<LineSegment> lineSegments = Arrays.asList(
                new LineSegment(rect.getTopLeft(), rect.getTopRight()),
                new LineSegment(rect.getTopRight(), rect.getBottomRight()),
                new LineSegment(rect.getBottomRight(), rect.getBottomLeft()),
                new LineSegment(rect.getBottomLeft(), rect.getTopLeft())
        );

        return lineSegments;
    }
    //endregion


    //region ===================== Interfaces ======================

    public interface Callback {
        void onFinish(MapMarker marker);
    }

    //endregion


}
