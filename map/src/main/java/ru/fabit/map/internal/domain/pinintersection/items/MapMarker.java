package ru.fabit.map.internal.domain.pinintersection.items;


import ru.fabit.map.internal.domain.pinintersection.WorldCoordinate;

public class MapMarker extends BaseMapElement {

    private final WorldCoordinate coordinate;

    public MapMarker(String id,
                     WorldCoordinate coordinate) {
        super(id);
        this.coordinate = coordinate;
    }

    public WorldCoordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        return "MapMarker{" +
                "coordinate=" + coordinate +
                '}';
    }
}
