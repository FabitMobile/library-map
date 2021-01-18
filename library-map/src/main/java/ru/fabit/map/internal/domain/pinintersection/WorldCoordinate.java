package ru.fabit.map.internal.domain.pinintersection;

public class WorldCoordinate {

    private final double lat;
    private final double lng;

    public WorldCoordinate(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
