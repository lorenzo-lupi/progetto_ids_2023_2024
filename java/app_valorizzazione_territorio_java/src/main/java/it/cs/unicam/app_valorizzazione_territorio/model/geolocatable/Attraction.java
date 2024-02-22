package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.AttractionOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represents an attraction, that is a particular point of interest associated with
 * a generic object of interest present on a territory that is always accessible to the public.
 */
@Setter
@Getter
@Entity
@NoArgsConstructor(force = true)
public class Attraction extends PointOfInterest{
    private AttractionTypeEnum type;
    public Attraction(String title, String description, Position position, Municipality municipality, AttractionTypeEnum type, User user) {
        super(title, description, position, municipality, user);
        this.type = type;
    }

    @Override
    @Transient
    public AttractionOF getOutputFormat() {
        return new AttractionOF(super.getOutputFormat(), this.getType().toString());
    }

    @Override
    @Transient
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parametersMapping = new HashMap<>(super.getParametersMapping());
        parametersMapping.put(Parameter.ATTRACTION_TYPE, getType());
        return parametersMapping;
    }

    @Override
    @Transient
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        Map<Parameter, Consumer<Object>> settersMapping = new HashMap<>(super.getSettersMapping());
        settersMapping.put(Parameter.ATTRACTION_TYPE, toObjectSetter(this::setType, AttractionTypeEnum.class));
        return settersMapping;
    }
}
