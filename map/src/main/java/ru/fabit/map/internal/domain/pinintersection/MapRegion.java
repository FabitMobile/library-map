package ru.fabit.map.internal.domain.pinintersection;

import java.util.Objects;

import ru.fabit.map.internal.domain.entity.Rect;

public class MapRegion {

    private final double minLat;
    private final double maxLat;

    private final double minLon;
    private final double maxLon;

    public MapRegion(double minLat, double maxLat, double minLon, double maxLon) {

        this.minLat = minLat;
        this.maxLat = maxLat;

        this.minLon = minLon;
        this.maxLon = maxLon;

    }

    public double getMinLat() {
        return minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public double latWidth() {
        return maxLat - minLat;
    }

    public double lonWidth() {
        return maxLon - minLon;
    }

    public Rect makeRect() {
        Rect rect = new Rect(minLat, maxLat, minLon, maxLon);
        return rect;
    }

    public boolean intersects(MapRegion mapRegion) {
        return this.minLon < mapRegion.maxLon
                && mapRegion.minLon < this.maxLon
                && this.maxLat > mapRegion.minLat
                && mapRegion.maxLat > this.minLat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapRegion mapRegion = (MapRegion) o;
        return Double.compare(mapRegion.minLat, minLat) == 0 &&
                Double.compare(mapRegion.maxLat, maxLat) == 0 &&
                Double.compare(mapRegion.minLon, minLon) == 0 &&
                Double.compare(mapRegion.maxLon, maxLon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minLat, maxLat, minLon, maxLon);
    }
}
