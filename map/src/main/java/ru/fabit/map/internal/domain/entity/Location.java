package ru.fabit.map.internal.domain.entity;

import java.util.ArrayList;
import java.util.List;

import ru.fabit.map.internal.domain.entity.MapCoordinates;


public class Location {

    public final static String POINT = "Point";
    public final static String POLYGON = "Polygon";
    public final static String LINE_STRING = "LineString";

    private String type;

    private List<Double> pointCoordinates;

    private List<List<Double>> lineStringCoordinates;

    private List<List<List<Double>>> polygonCoordinates;

    private MapCoordinates pointMapCoordinate;

    private List<MapCoordinates> lineStringMapCoordinates;

    private List<List<MapCoordinates>> polygonMapCoordinate;


    //region ===================== Getters ======================

    public String getType() {
        return type;
    }

    public MapCoordinates getPointMapCoordinate() {
        if (pointMapCoordinate == null) {
            pointMapCoordinate = new MapCoordinates(pointCoordinates.get(1), pointCoordinates.get(0));
        }
        return pointMapCoordinate;
    }

    public List<MapCoordinates> getLineStringMapCoordinates() {
        if (lineStringMapCoordinates == null) {
            lineStringMapCoordinates = new ArrayList<>();
            for (int i = 0; i < lineStringCoordinates.size(); i++) {
                lineStringMapCoordinates.add(
                        new MapCoordinates(lineStringCoordinates.get(i).get(1),
                                lineStringCoordinates.get(i).get(0))
                );
            }
        }
        return lineStringMapCoordinates;
    }

    public List<List<MapCoordinates>> getPolygonMapCoordinate() {
        if (polygonMapCoordinate == null) {
            polygonMapCoordinate = new ArrayList<>();
            for (int i = 0; i < polygonCoordinates.size(); i++) {
                polygonMapCoordinate.add(new ArrayList<>());
                for (int j = 0; j < polygonCoordinates.get(i).size(); j++) {
                    polygonMapCoordinate.get(i).add(new MapCoordinates(
                            polygonCoordinates.get(i).get(j).get(1),
                            polygonCoordinates.get(i).get(j).get(0)));
                }
            }
        }
        return polygonMapCoordinate;
    }

    //endregion

    //region ===================== Setters ======================

    public void setType(String type) {
        this.type = type;
    }

    public void setPointCoordinates(List<Double> pointCoordinates) {
        this.pointCoordinates = pointCoordinates;
    }

    public void setLineStringCoordinates(List<List<Double>> lineStringCoordinates) {
        this.lineStringCoordinates = lineStringCoordinates;
    }

    public void setPolygonCoordinates(List<List<List<Double>>> polygonCoordinates) {
        this.polygonCoordinates = polygonCoordinates;
    }

    public void setPointMapCoordinate(MapCoordinates pointMapCoordinate) {
        this.pointMapCoordinate = pointMapCoordinate;
    }

    public void setLineStringMapCoordinates(List<MapCoordinates> lineStringMapCoordinates) {
        this.lineStringMapCoordinates = lineStringMapCoordinates;
    }

    public void setPolygonMapCoordinate(List<List<MapCoordinates>> polygonMapCoordinate) {
        this.polygonMapCoordinate = polygonMapCoordinate;
    }

    //endregion
}
