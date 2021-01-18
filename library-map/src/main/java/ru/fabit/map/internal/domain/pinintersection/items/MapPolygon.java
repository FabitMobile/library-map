package ru.fabit.map.internal.domain.pinintersection.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.fabit.map.internal.domain.pinintersection.MapRegion;
import ru.fabit.map.internal.domain.pinintersection.WorldCoordinate;

public class MapPolygon extends BaseMapElement {

    private final List<WorldCoordinate> worldCoordinates;

    public MapPolygon(String id, List<WorldCoordinate> worldCoordinates) {
        super(id);
        this.worldCoordinates = worldCoordinates;
    }

    public List<WorldCoordinate> getWorldCoordinates() {
        return worldCoordinates;
    }

    public MapRegion makeBoundingRegion() {

        List<Double> lats = new ArrayList<>();
        List<Double> lngs = new ArrayList<>();

        for (WorldCoordinate worldCoordinate : worldCoordinates) {
            lats.add(worldCoordinate.getLat());
            lngs.add(worldCoordinate.getLng());
        }

        double minLat = Collections.min(lats);
        double maxLat = Collections.max(lats);
        double minLon = Collections.min(lngs);
        double maxLon = Collections.max(lngs);

        return new MapRegion(
                minLat,
                maxLat,
                minLon,
                maxLon
        );
    }
}
