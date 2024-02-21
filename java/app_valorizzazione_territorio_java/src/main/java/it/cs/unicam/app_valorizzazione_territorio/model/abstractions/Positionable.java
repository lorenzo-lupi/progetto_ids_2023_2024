package it.cs.unicam.app_valorizzazione_territorio.model.abstractions;

import it.cs.unicam.app_valorizzazione_territorio.osm.Position;

/**
 * Class implementing this interface represent objects that have a position,
 * expressed as a {@link Position} object with latitude and longitude.
 */
public interface Positionable {
    /**
     * Returns the geographical position associated to the object.
     *
     * @return the geographical position associated to the object
     */
    Position getPosition();
}
