package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Positionable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Position;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchEngine;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MapProviderBase implements MapProvider{

    public MapProviderBase() {
    }

    @Override
    public Map<?> getEmptyMap(Municipality municipality) throws IOException {
        return getEmptyMap(municipality.getCoordinatesBox());
    }

    @Override
    public Map<?> getEmptyMap(CoordinatesBox box) throws IOException {
        return new MapBuilder<>()
                .buildOsmData(box)
                .build()
                .getResult();
    }

    @Override
    public Map<GeoLocatable> getMap(Municipality municipality) throws IOException {
        return getFilteredMap(municipality, municipality.getCoordinatesBox(), List.of());
    }

    @Override
    public Map<GeoLocatable> getMap(Municipality municipality, CoordinatesBox box) throws IOException {
        return getFilteredMap(municipality, box, List.of());
    }

    @Override
    public Map<Municipality> getMunicipalitiesMap() throws IOException {
        return fact(MunicipalityRepository.getInstance().getItemStream().toList(), CoordinatesBox.ITALY);
    }

    @Override
    public Map<GeoLocatable> getFilteredMap(Municipality municipality,
                                                   List<SearchFilter> filters) throws IOException {
        return getFilteredMap(municipality, municipality.getCoordinatesBox(), filters);
    }

    @Override
    public Map<GeoLocatable> getFilteredMap(Municipality municipality,
                                                   CoordinatesBox box,
                                                   List<SearchFilter> filters) throws IOException {

        if(municipality == null || box == null || filters == null)
            throw new IllegalArgumentException("The arguments cannot be null");

        if(!municipality.getCoordinatesBox().contains(box))
            throw new IllegalArgumentException("The box must be contained in the municipality box");

        filters = new LinkedList<>(filters);
        filters.add(new SearchFilter(Parameter.POSITION.toString(), "INCLUDED_IN_BOX", box));

        SearchEngine<GeoLocatable> geoLocatableSearchEngine = new SearchEngine<> (municipality.getGeoLocatables().stream().filter(GeoLocatable::isApproved).toList());

        filters.forEach(filter -> geoLocatableSearchEngine.addCriterion(Parameter.valueOf(filter.parameter()),
                SearchCriterion.stringToBiPredicate.get(filter.predicate()), filter.value()));

        return fact(geoLocatableSearchEngine.search().getResults(), box);
    }

    @Override
    public Municipality getMunicipalityByPosition(Position position) throws IOException {
        String municipalityName = OSMRequestHandler.getInstance().getMunicipalityOfPosition(position);
        return MunicipalityRepository.getInstance().getItemStream()
                .filter(m -> m.getName().equals(municipalityName))
                .findFirst()
                .orElse(null);
    }

    private static <P extends Positionable & Visualizable> Map<P> fact(List<P> geoLocatables, CoordinatesBox box) throws IOException {
        return new MapBuilder<P>()
                .buildOsmData(box)
                .buildPointsList(geoLocatables)
                .build()
                .getResult();
    }
}
