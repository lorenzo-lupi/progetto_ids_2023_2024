package it.cs.unicam.app_valorizzazione_territorio.model;

public class Position {
    private final double latitude;
    private final double longitude;

    public Position(double latitude, double longitude) {
        if(Double.isNaN(latitude) || Double.isNaN(longitude))
            throw new IllegalArgumentException("Latitude and longitude must be a number");
        this.latitude = latitude;
        this.longitude = longitude;
    }

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
