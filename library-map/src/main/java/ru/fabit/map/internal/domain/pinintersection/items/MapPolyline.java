package ru.fabit.map.internal.domain.pinintersection.items;

import java.util.List;

import ru.fabit.map.internal.domain.pinintersection.WorldCoordinate;


public class MapPolyline extends BaseMapElement {

    private final List<WorldCoordinate> worldCoordinates;

    public MapPolyline(String id, List<WorldCoordinate> worldCoordinates) {
        super(id);
        this.worldCoordinates = worldCoordinates;
    }

    public List<WorldCoordinate> getWorldCoordinates() {
        return worldCoordinates;
    }
}
