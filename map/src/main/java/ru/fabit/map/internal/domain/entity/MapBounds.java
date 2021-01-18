package ru.fabit.map.internal.domain.entity;

import java.util.Arrays;
import java.util.List;


public class MapBounds {

    private final Double minLat;
    private final Double maxLat;
    private final Double minLon;
    private final Double maxLon;

    public MapBounds(Double minLat, Double maxLat, Double minLon, Double maxLon) {
        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLon = minLon;
        this.maxLon = maxLon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapBounds mapBounds = (MapBounds) o;

        if (minLat != null ? !minLat.equals(mapBounds.minLat) : mapBounds.minLat != null)
            return false;
        if (maxLat != null ? !maxLat.equals(mapBounds.maxLat) : mapBounds.maxLat != null)
            return false;
        if (minLon != null ? !minLon.equals(mapBounds.minLon) : mapBounds.minLon != null)
            return false;
        return maxLon != null ? maxLon.equals(mapBounds.maxLon) : mapBounds.maxLon == null;
    }

    @Override
    public int hashCode() {
        int result = minLat != null ? minLat.hashCode() : 0;
        result = 31 * result + (maxLat != null ? maxLat.hashCode() : 0);
        result = 31 * result + (minLon != null ? minLon.hashCode() : 0);
        result = 31 * result + (maxLon != null ? maxLon.hashCode() : 0);
        return result;
    }
//region ===================== Getters ======================

    public Double getMinLat() {
        return minLat;
    }

    public Double getMaxLat() {
        return maxLat;
    }

    public Double getMinLon() {
        return minLon;
    }

    public Double getMaxLon() {
        return maxLon;
    }

    //endregion

    //region ===================== Mapping ======================

    public static List<Double> map(MapBounds mapBounds) {
        return Arrays.asList(mapBounds.getMinLat(), mapBounds.getMaxLat(), mapBounds.getMinLon(), mapBounds.getMaxLon());
    }

    public static MapBounds map(List<Double> doubleList) {
        return new MapBounds(doubleList.get(0), doubleList.get(1), doubleList.get(2), doubleList.get(3));
    }

    //endregion

    //region ===================== ToString ======================

    @Override
    public String toString() {
        return "MapBounds{" +
                "minLat=" + minLat +
                ", maxLat=" + maxLat +
                ", minLon=" + minLon +
                ", maxLon=" + maxLon +
                '}';
    }

    //endregion

}
