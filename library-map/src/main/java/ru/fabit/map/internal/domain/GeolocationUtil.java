package ru.fabit.map.internal.domain;


import ru.fabit.map.internal.domain.entity.MapCoordinates;

public class GeolocationUtil {

    //Количество метров в 1 градусе
    private static final int METERS_PER_DEGREE = 111120;

    public static double getDistanceInMetre(MapCoordinates a, MapCoordinates b) {
        return METERS_PER_DEGREE * lengthVector(a, b);
    }

    public static double getDistanceInDegree(MapCoordinates a, MapCoordinates b) {
        return lengthVector(a, b);
    }

    public static double getDistanceInDegree(double aX, double aY, double bX, double bY) {
        return lengthVector(aX, aY, bX, bY);
    }


    public static double distanceInMetersToDegree(double distance) {
        return distance / METERS_PER_DEGREE;
    }


    public static double distanceInDegreeToMeters(double gradus) {
        return gradus * METERS_PER_DEGREE;
    }

    //Формула длины вектора
    private static double lengthVector(MapCoordinates a, MapCoordinates b) {
        return Math.sqrt(Math.pow(b.getLongitude() - a.getLongitude(), 2) + Math.pow(b.getLatitude() - a.getLatitude(), 2));
    }

    private static double lengthVector(double aX, double aY, double bX, double bY) {
        return Math.sqrt(Math.pow(bX - aX, 2) + Math.pow(bY - aY, 2));
    }
}
