package it.cs.unicam.app_valorizzazione_territorio.dtos;

/**
 * This class represents an attraction Detailed Output Format object.
 *
 * @param name
 * @param description
 * @param position
 * @param municipalitySOF
 * @param classification
 * @param images
 * @param contents
 * @param type
 * @param ID
 */
public final class AttractionDOF extends PointOfInterestDOF {
    private final String type;
    public AttractionDOF(PointOfInterestDOF pointOfInterestDOF, String type) {
        super(pointOfInterestDOF.name(),
                pointOfInterestDOF.description(),
                pointOfInterestDOF.position(),
                pointOfInterestDOF.municipalitySOF(),
                pointOfInterestDOF.classification(),
                pointOfInterestDOF.images(),
                pointOfInterestDOF.contents(),
                pointOfInterestDOF.ID());
        this.type = type;
    }

    public String type() {
        return type;
    }
}
