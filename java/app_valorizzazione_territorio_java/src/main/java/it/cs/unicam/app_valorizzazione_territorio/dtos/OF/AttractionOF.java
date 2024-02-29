package it.cs.unicam.app_valorizzazione_territorio.dtos.OF;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;

/**
 * This class represents an attraction Detailed Output Format object.
 */
@JsonView(View.Detailed.class)
public final class AttractionOF extends PointOfInterestOF {
    @JsonView(View.Detailed.class)
    private final String type;
    public AttractionOF(PointOfInterestOF pointOfInterestOF, String type) {
        super(pointOfInterestOF.name(),
                pointOfInterestOF.description(),
                pointOfInterestOF.position(),
                pointOfInterestOF.municipalityName(),
                pointOfInterestOF.classification(),
                pointOfInterestOF.representativeFile(),
                pointOfInterestOF.files(),
                pointOfInterestOF.contents(),
                pointOfInterestOF.ID());
        this.type = type;
    }

    public String type() {
        return type;
    }
}
