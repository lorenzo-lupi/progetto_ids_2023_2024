package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.LinkedList;

public class PointOfInterestBuilder extends GeoLocatableBuilder<PointOfInterest> {
    private Position position;
    public PointOfInterestBuilder(Municipality municipality, User user) {
        super(municipality);
    }

    public void setPosition(Position position) {
        if(position == null)
            throw new IllegalArgumentException("Position must not be null");
        if(!this.getMunicipality().getCoordinatesBox().contains(position))
            throw new IllegalArgumentException("Position must be inside the municipality");

        this.position = position;
    }
    @Override
    public PointOfInterest obtainResult() throws IllegalStateException {
        return new PointOfInterest(this.getTitle(),
                                                        this.getDescription(),
                                                        this.position,
                                                        this.getMunicipality(),
                                                        this.getImages(),
                                                        new LinkedList<>());
    }
}