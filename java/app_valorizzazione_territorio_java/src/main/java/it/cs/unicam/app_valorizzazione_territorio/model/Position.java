package it.cs.unicam.app_valorizzazione_territorio.model;

/**
 * This class represents a geographical position, that is a
 * point on the surface of the Earth associated with geographical coordinates.
 */
public class Position {
    private final double latitude;
    private final double longitude;

    /**
     * Constructor for a geographical position.
     */
    public Position(double latitude, double longitude) {
        if(Double.isNaN(latitude) || Double.isNaN(longitude)
                || Double.compare(latitude, -90) < 0 || Double.compare(latitude, 90) > 0
                || Double.compare(longitude, -180) < 0 || Double.compare(longitude, 180) > 0)
            throw new IllegalArgumentException("Latitude and longitude must be valid numbers");
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructor for a geographical position.
     */
    public Position(Position position) {
        this.latitude = position.getLatitude();
        this.longitude = position.getLongitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    @Override
    public String toString() {
        return "Position{" +
                "latitude=" + latitude +
                ", longitude=" + longitude + '}';
    }

}
