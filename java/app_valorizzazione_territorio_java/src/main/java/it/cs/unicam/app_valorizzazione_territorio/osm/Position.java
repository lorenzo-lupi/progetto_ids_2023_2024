package it.cs.unicam.app_valorizzazione_territorio.osm;


import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import jakarta.persistence.Embeddable;

/**
 * This class represents a geographical position, that is a
 * point on the surface of the Earth associated with geographical coordinates.
 */
@JsonView(View.Synthesized.class)
@Embeddable
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Position position)) {
            return false;
        }
        return Double.compare(latitude, position.getLatitude()) == 0 &&
                Double.compare(longitude, position.getLongitude()) == 0;
    }

    @Override
    public String toString() {
        return "Position{" +
                "latitude=" + latitude +
                ", longitude=" + longitude + '}';
    }

    /**
     * Returns the distance between two positions.
     *
     * @param other the other position
     * @return the distance between two positions
     */
    public Position sum(Position other){
        return new Position(this.latitude + other.getLatitude(),
                this.longitude + other.getLongitude());
    }

    /**
     * Returns the distance between two positions.
     *
     * @param scalar the scalar to apply
     * @return the distance between two positions
     */
    public Position divide(int scalar){
        return new Position(this.latitude / scalar,
                this.longitude / scalar);
    }


}
