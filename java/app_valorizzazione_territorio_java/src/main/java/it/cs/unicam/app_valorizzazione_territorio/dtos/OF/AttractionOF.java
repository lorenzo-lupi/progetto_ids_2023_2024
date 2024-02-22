package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;

/**
 * This class represents an attraction Detailed Output Format object.
 */
@JsonView(View.Detailed.class)
public final class AttractionOF extends PointOfInterestOF {
    private final String type;
    public AttractionOF(PointOfInterestOF pointOfInterestOF, String type) {
        super(pointOfInterestOF.name(),
                pointOfInterestOF.description(),
                pointOfInterestOF.position(),
                pointOfInterestOF.municipality(),
                pointOfInterestOF.classification(),
                pointOfInterestOF.representativeImage(),
                pointOfInterestOF.images(),
                pointOfInterestOF.contents(),
                pointOfInterestOF.ID());
        this.type = type;
    }

    public String type() {
        return type;
    }
}
