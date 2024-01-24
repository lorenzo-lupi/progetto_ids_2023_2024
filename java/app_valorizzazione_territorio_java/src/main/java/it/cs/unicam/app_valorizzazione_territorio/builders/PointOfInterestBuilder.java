package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.LinkedList;

public class PointOfInterestBuilder extends GeoLocatableBuilder<PointOfInterest> {
    private Position position;
    private PointOfInterest pointOfInterest;
    public PointOfInterestBuilder(Municipality municipality) {
        super(municipality);
    }




    public GeoLocatableBuilder<PointOfInterest> setPosition(Position position) {
        if(position == null)
            throw new IllegalArgumentException("Position must not be null");
        if(!this.getMunicipality().getCoordinatesBox().contains(position))
            throw new IllegalArgumentException("Position must be inside the municipality");

        this.position = position;
        return this;
    }

    @Override
    public void build() throws IllegalStateException {
        this.checkArguments();
        this.pointOfInterest = new PointOfInterest(this.getTitle(),
                this.getDescription(),
                this.position,
                this.getMunicipality(),
                this.getImages(),
                new LinkedList<>());
    }

    @Override
    public PointOfInterest obtainResult() throws IllegalStateException {
        if (pointOfInterest == null)
            throw new IllegalStateException("PointOfInterest not built");
        return this.pointOfInterest;
    }

    @Override
    public void checkArguments(){
        super.checkArguments();
        if(this.position == null)
            throw new IllegalStateException("Position not set");
    }
}
