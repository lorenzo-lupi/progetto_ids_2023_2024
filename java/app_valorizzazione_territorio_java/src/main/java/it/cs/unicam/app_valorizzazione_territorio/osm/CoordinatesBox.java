package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.Position;

/**
 * This class represents a geographical box, that is a rectangle on the surface of the Earth.
 * The internal representation is a pair of two geographical positions, the north-west and the south-east corners.
 */
public class CoordinatesBox {
    public static final CoordinatesBox ITALY = new CoordinatesBox(
            new Position(47.092, 6.626),
            new Position(35.492, 18.520)
    );

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
        if (northWest.getLatitude() < southEast.getLatitude() || northWest.getLongitude() > southEast.getLongitude())
            throw new IllegalArgumentException("North-west corner must be north and west of the south-east corner");
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

    public double getNorth() {
        return northWest.getLatitude();
    }

    public double getSouth() {
        return southEast.getLatitude();
    }

    public double getWest() {
        return northWest.getLongitude();
    }

    public double getEast() {
        return southEast.getLongitude();
    }

    /**
     * Returns true if the geographical box contains the given geographical position.
     *
     * @param position the geographical position to check
     * @return true if the geographical box contains the given geographical position, false otherwise
     */
    public boolean contains(Position position) {
        return position.getLatitude() <= getNorth() &&
                position.getLatitude() >= getSouth() &&
                position.getLongitude() >= getWest() &&
                position.getLongitude() <= getEast();
    }

    /**
     * Returns true if the geographical box contains the given geographical box.
     * @param box the geographical box to check
     * @return true if the geographical box contains the given geographical box, false otherwise
     */
    public boolean contains(CoordinatesBox box){
        if(box == null)
            throw new IllegalArgumentException("The box must not be null");

        return box.getNorth() <= getNorth() &&
                box.getSouth() >= getSouth() &&
                box.getWest() >= getWest() &&
                box.getEast() <= getEast();
    }

    @Override
    public String toString() {
        return "CoordinatesBox{" +
                "northWest=" + northWest +
                ", southEast=" + southEast +
                '}';
    }
}
