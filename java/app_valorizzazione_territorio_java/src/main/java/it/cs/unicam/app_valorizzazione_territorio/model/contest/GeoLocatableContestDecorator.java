package it.cs.unicam.app_valorizzazione_territorio.model.contest;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@DiscriminatorValue("GeoLocatable")
@NoArgsConstructor(force = true)
public class GeoLocatableContestDecorator extends ContestDecorator{

    @Transient
    // TODO :
    //  @ManyToOne(fetch = FetchType.EAGER)
    //  @JoinColumn(name = "geoLocatable_id") //Attenzione, forse non è il nome giusto, provare con geo_locatable_id
    private final GeoLocatable geoLocatable;

    public GeoLocatableContestDecorator(Contest contest, GeoLocatable geoLocatable) {
        super(contest);
        if(geoLocatable == null)
            throw new IllegalArgumentException("GeoLocatable must not be null");
        this.geoLocatable = geoLocatable;
    }

    @Override
    public boolean hasGeoLocation() {
        return true;
    }

    @Override
    public GeoLocatable getGeoLocation() throws UnsupportedOperationException {
        return this.geoLocatable;
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        Map<Parameter, Object> parametersMapping = new HashMap<>(super.getParametersMapping());
        parametersMapping.put(Parameter.THIS, this);
        parametersMapping.put(Parameter.CONTEST_TYPE, "GeoLocatable");
        parametersMapping.put(Parameter.POSITION, this.geoLocatable.getPosition());
        return parametersMapping;
    }
}