package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.AttractionDOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represents an attraction, that is a particular point of interest associated with
 * a generic object of interest present on a territory that is always accessible to the public.
 */
public class Attraction extends PointOfInterest{
    private AttractionTypeEnum type;
    public Attraction(String title, String description, Position position, Municipality municipality, AttractionTypeEnum type, User user) {
        super(title, description, position, municipality, user);
        this.type = type;
    }

    public AttractionTypeEnum getType() {
        return type;
    }

    public void setType(AttractionTypeEnum type) {
        this.type = type;
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

    @Override
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        Map<Parameter, Consumer<Object>> settersMapping = new HashMap<>(super.getSettersMapping());
        settersMapping.put(Parameter.ATTRACTION_TYPE, toObjectSetter(this::setType, AttractionTypeEnum.class));
        return settersMapping;
    }
}
