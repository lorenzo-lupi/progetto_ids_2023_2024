package it.cs.unicam.app_valorizzazione_territorio.model;

/**
 * This class represents a geographical box, that is a rectangle on the surface of the Earth.
 * The internal representation is a pair of two geographical positions, the north-west and the south-east corners.
 */
public class CoordinatesBox {
    private final Position northWest;
    private final Position southEast;

    /**
     * Constructor for a geographical box.
     *
     * @param northWest the north-west corner of the geographical box
     * @param southEast the south-east corner of the geographical box
     */
    public CoordinatesBox(Position northWest, Position southEast) {
        if (northWest == null || southEast == null)
            throw new IllegalArgumentException("North-west and south-east corners must not be null");
        this.northWest = northWest;
        this.southEast = southEast;
    }

    public Position getNorthWest() {
        return northWest;
    }

    public Position getNorthEast() {
        return new Position(northWest.getLatitude(), southEast.getLongitude());
    }

    public Position getSouthEast() {
        return southEast;
    }

    public Position getSouthWest() {
        return new Position(southEast.getLatitude(), northWest.getLongitude());
    }

    @Override
    public String toString() {
        return "CoordinatesBox{" +
                "northWest=" + northWest +
                ", southEast=" + southEast +
                '}';
    }
}