package it.cs.unicam.app_valorizzazione_territorio.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.AttractionDOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an attraction, that is a particular point of interest associated with
 * a generic object of interest present on a territory that is always accessible to the public.
 */
public class Attraction extends PointOfInterest{
    private final AttractionTypeEnum type;
    public Attraction(String title, String description, Position position, Municipality municipality, AttractionTypeEnum type) {
        super(title, description, position, municipality);
        this.type = type;
    }

    public AttractionTypeEnum getType() {
        return type;
    }

    @Override
    public AttractionDOF getDetailedFormat() {
        return new AttractionDOF(super.getDetailedFormat(), this.getType().toString());
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parametersMapping = new HashMap<>(super.getParametersMapping());
        parametersMapping.put(Parameter.ATTRACTION_TYPE, getType());
        return parametersMapping;
    }
}
